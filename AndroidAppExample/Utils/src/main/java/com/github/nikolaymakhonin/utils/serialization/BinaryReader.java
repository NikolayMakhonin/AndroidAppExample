package com.github.nikolaymakhonin.utils.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.utils.contracts.patterns.ILocker;
import com.github.nikolaymakhonin.utils.strings.CharsetUtils;
import com.github.nikolaymakhonin.utils.time.DateTime;
import com.github.nikolaymakhonin.utils.time.TimeSpan;

import rx.functions.Action1;
import rx.functions.Func0;

public class BinaryReader {
    
    public final InputStream _stream;
    private final ByteBuffer _buffer;
    private final int BUFFER_MIN_SIZE = 16;
    private int _bufferSize;
    
    public boolean ReadingFullBuffer;
    
    public BinaryReader(final InputStream stream) {
        this(stream, 4096);
    }
    
    public BinaryReader(final InputStream stream, final int bufferSize) {
        _stream = stream;
        _buffer = ByteBuffer.allocate(Math.max(BUFFER_MIN_SIZE, bufferSize));
        _buffer.order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public byte readByte() {
        flush(1);
        return _buffer.get();
    }
    
    public int readInt() {
        flush(4);
        return _buffer.getInt();
    }
    
    public long readLong() {
        flush(8);
        return _buffer.getLong();
    }
    
    public char readChar() {
        flush(2);
        return _buffer.getChar();
    }
    
    public String readString() {
        return readString(CharsetUtils.UTF8);
    }
    
    public String readString(final Charset charset) {
        final int length = readInt();
        final byte[] bytes = new byte[length];
        read(bytes);
        return new String(bytes, charset);
    }
    
    public double readDouble() {
        flush(8);
        return _buffer.getDouble();
    }
    
    public float readFloat() {
        flush(4);
        return _buffer.getFloat();
    }
    
    public short readShort() {
        flush(2);
        return _buffer.getShort();
    }
    
    public boolean readBoolean() {
        flush(1);
        return _buffer.get() != 0;
    }
    
    private void flush(final int mustAllowedSize) {
        int oldPosition = _buffer.position();
        final int oldBufferSize = _bufferSize;
        final int remainingSize = oldBufferSize - oldPosition;
        if (remainingSize >= mustAllowedSize) {
            return;
        }
        final int bufferLimit = _buffer.limit();
        final int maxReadCount = ReadingFullBuffer ? bufferLimit : mustAllowedSize - remainingSize;

        if (oldPosition > 0 && maxReadCount + oldBufferSize > bufferLimit) { 
            byte[] remainingBytes = null;
            if (remainingSize > 0) {
                remainingBytes = new byte[remainingSize];
                _buffer.get(remainingBytes, 0, remainingSize);
            }
            _buffer.clear();
            _bufferSize = 0;
            if (remainingBytes != null) {
                _buffer.put(remainingBytes);
                _bufferSize += remainingSize;
            }
            oldPosition = 0;
        }
        else
        {
            _buffer.position(oldBufferSize);
        }
        final byte[] newBytes = readStream(maxReadCount);
        if (newBytes != null) {
            _buffer.put(newBytes, 0, newBytes.length);
            _bufferSize += newBytes.length;
        }
        _buffer.position(oldPosition);
    }
    
    public void read(final byte[] bytes) {
        read(bytes, 0, bytes.length);
    }
    
    public int read(final byte[] bytes, final int offset, final int count) {
        final int bufferLimit = _buffer.limit();
        int pos = 0;
        while (pos < count) {
            int remainingSize = _bufferSize - _buffer.position();
            int blockSize = Math.min(bufferLimit, count - pos);
            if (remainingSize == 0) {
                flush(blockSize);
                remainingSize = _bufferSize - _buffer.position();
                if (remainingSize == 0) { return pos; }
            }
            blockSize = Math.min(remainingSize, blockSize);
            _buffer.get(bytes, offset + pos, blockSize);
            pos += blockSize;
        }
        return pos;
    }
    
    protected byte[] readStream(int maxCount) {
        try {
            final int availableSize = _stream.available();
            maxCount = Math.min(availableSize, maxCount);
            if (maxCount == 0) {
                return null;
            }
            final byte[] buffer = new byte[maxCount];
            final int readCount = _stream.read(buffer, 0, maxCount);
            if (readCount != maxCount) {
                Log.e("BinaryReader", "readCount < maxCount");
                return null;
            }
            return buffer;
        } catch (final IOException e) {
            Log.e("BinaryReader", "", e);
            return null;
        }
    }
    
//    protected int readStream(final byte[] buffer, final int offset, int count) {
//        try {
//            final int availableSize = _stream.available();
//            count = Math.min(availableSize, count);
//            if (count == 0) { return 0; }
//            return _stream.read(buffer, offset, count);
//        } catch (final IOException e) {
//            Log.e("BinaryReader", "", e);
//            return 0;
//        }
//    }
    
    // region Read Collections
    public <TCollection extends Collection<TItem>, TItem> TCollection ReadCollection(
        final Class<? extends TCollection> type, final ReadItemFunc<TItem> readItem) throws Exception {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        final int count = readInt();
        TCollection result;
        try {
            result = type.newInstance();
        } catch (final Exception e) {
            Log.e("BinaryReaderUtils", "", e);
            for (int i = 0; i < count; i++) {
                readItem.invoke(this);
            }
            return null;
        }
        for (int i = 0; i < count; i++) {
            result.add(readItem.invoke(this));
        }
        return result;
    }
    
    public <TItem> int ReadCollection(final Collection<TItem> collection,
        final ReadItemFunc<TItem> readItem) throws Exception {
        final boolean isNull = readBoolean();
        if (isNull) { return 0; }
        final int count = readInt();
        if (collection instanceof ILocker) {
            synchronized(((ILocker)collection).Locker()) {
                for (int i = 0; i < count; i++) {
                    collection.add(readItem.invoke(this));
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                collection.add(readItem.invoke(this));
            }
        }
        return count;
    }
    
    public int ReadCollection(final ReadItemAction readAndAddItem) throws Exception {
        return ReadCollection(readAndAddItem, null);
    }
    
    public int ReadCollection(final ReadItemAction readAndAddItem,
        final Action1<Integer> initCount) throws Exception {
        final boolean isNull = readBoolean();
        if (isNull) { return 0; }
        final int count = readInt();
        if (initCount != null) {
            initCount.call(count);
        }
        for (int i = 0; i < count; i++) {
            readAndAddItem.call(this);
        }
        return count;
    }
    
    public <TItem> TItem[] ReadArray(final Class<? extends TItem[]> type, final ReadItemFunc<TItem> readItem) throws Exception {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        final int count = readInt();
        final TItem[] result = (TItem[]) Array.newInstance(type.getComponentType(), count);
        for (int i = 0; i < count; i++) {
            result[i] = readItem.invoke(this);
        }
        return result;
    }

    public boolean[] ReadBooleanArray(final ReadItemFunc<Boolean> readItem) throws Exception {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        final int count = readInt();
        final boolean[] result = new boolean[count];
        for (int i = 0; i < count; i++) {
            result[i] = readItem.invoke(this);
        }
        return result;
    }

    public int[] ReadIntegerArray(final ReadItemFunc<Integer> readItem) throws Exception {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        final int count = readInt();
        final int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = readItem.invoke(this);
        }
        return result;
    }

    public double[] ReadDoubleArray(final ReadItemFunc<Double> readItem) throws Exception {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        final int count = readInt();
        final double[] result = new double[count];
        for (int i = 0; i < count; i++) {
            result[i] = readItem.invoke(this);
        }
        return result;
    }

    public byte[] ReadByteArray() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        final int count = readInt();
        final byte[] buffer = new byte[count];
        read(buffer);
        return buffer;
    }
    
    private static class MapEntryReader<TKey, TValue> implements ReadItemAction {
        private final ReadItemFunc<TKey>   _readKey;
        private final ReadItemFunc<TValue> _readValue;
        private final Map<TKey, TValue>    _dictionary;
        
        public MapEntryReader(final ReadItemFunc<TKey> readKey, final ReadItemFunc<TValue> readValue,
            final Map<TKey, TValue> dictionary) {
            _readKey = readKey;
            _readValue = readValue;
            _dictionary = dictionary;
        }
        
        @Override
        public void call(final BinaryReader reader) throws Exception {
            final TKey key = _readKey.invoke(reader);
            final TValue value = _readValue.invoke(reader);
            _dictionary.put(key, value);
        }
    }
    
    public <TKey, TValue> int ReadDictionary(final Map<TKey, TValue> dictionary,
        final ReadItemFunc<TKey> readKey, final ReadItemFunc<TValue> readValue) throws Exception {
        if (dictionary instanceof ILocker) {
            synchronized(((ILocker)dictionary).Locker()) {
                return ReadCollection(new MapEntryReader<>(readKey, readValue, dictionary));
            }
        } else {
            return ReadCollection(new MapEntryReader<>(readKey, readValue, dictionary));
        }
    }
    
    public UUID ReadGuid() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return new UUID(readLong(), readLong());
    }
    
    @Deprecated
    public <T extends Parcelable> T ReadParcelable(final ClassLoader classLoader) {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        final byte[] buffer = ReadByteArray();
        final Parcel parcel = Parcel.obtain();
        try {
            parcel.unmarshall(buffer, 0, buffer.length);
            parcel.setDataPosition(0);
            return parcel.readParcelable(classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader());
        } finally {
            parcel.recycle();
        }
    }
    
    // endregion
    
    // region Nullable
    public <T extends IStreamSerializable> T ReadNullable(Class<T> valueClass) throws Exception {
        return ReadNullable(valueClass, null);
    }

    public <T extends IStreamSerializable> T ReadNullable(Class<T> valueClass, final Func0<T> constructor) throws Exception {
        final boolean hasValue = readBoolean();
        if (!hasValue) {
            return null;
        } else {
            T value = constructor != null
                ? constructor.call()
                : valueClass.getConstructor().newInstance();
            value.DeSerialize(this);
            return value;
        }
    }

    public <T> T ReadNullable(final ReadItemFunc<T> readItem) throws Exception {
        final boolean hasValue = readBoolean();
        return hasValue ? readItem.invoke(this) : null;
    }

    public String ReadNullableString() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readString();
    }
    
    public Integer ReadNullableInteger() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readInt();
    }
    
    public Long ReadNullableLong() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readLong();
    }
    
    public Short ReadNullableShort() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readShort();
    }
    
    public Byte ReadNullableByte() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readByte();
    }
    
    public Boolean ReadNullableBoolean() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readBoolean();
    }
    
    public Character ReadNullableCharacter() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readChar();
    }
    
    public Double ReadNullableDouble() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readDouble();
    }
    
    public Float ReadNullableFloat() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return readFloat();
    }

    public TimeSpan ReadNullableTimeSpan() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return new TimeSpan(readLong());
    }

    public DateTime ReadNullableDateTime() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        return new DateTime(readLong());
    }

    public URI ReadNullableURI() {
        final boolean isNull = readBoolean();
        if (isNull) { return null; }
        try {
            return new URI(readString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // endregion
    
    // region Readers
    
    public static final ReadItemFunc<UUID> UUIDReader = reader -> reader.ReadGuid();
    
    public static final ReadItemFunc<Long> LongReader = reader -> reader.readLong();
    
    public static final ReadItemFunc<Integer> IntegerReader = reader -> reader.readInt();
    
    public static final ReadItemFunc<String> StringReader = reader -> reader.ReadNullableString();
    
    // endregion
}
