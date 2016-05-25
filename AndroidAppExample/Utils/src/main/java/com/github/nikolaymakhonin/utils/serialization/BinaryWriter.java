package com.github.nikolaymakhonin.utils.serialization;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.utils.contracts.patterns.ILocker;
import com.github.nikolaymakhonin.utils.strings.CharsetUtils;
import com.github.nikolaymakhonin.utils.time.DateTime;
import com.github.nikolaymakhonin.utils.time.TimeSpan;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class BinaryWriter {
    
    public final OutputStream _stream;
    private final ByteBuffer _buffer;
    private final int BUFFER_MIN_SIZE = 16;
    
    public BinaryWriter(final OutputStream stream) {
        this(stream, 4096);
    }
    
    public BinaryWriter(final OutputStream stream, final int bufferSize) {
        _stream = stream;
        _buffer = ByteBuffer.allocate(Math.max(BUFFER_MIN_SIZE, bufferSize));
        _buffer.order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public void write(final byte value) {
        flush(1);
        _buffer.put(value);
    }
    
    public void write(final int value) {
        flush(4);
        _buffer.putInt(value);
    }
    
    public void write(final long value) {
        flush(8);
        _buffer.putLong(value);
    }
    
    public void write(final char value) {
        flush(2);
        _buffer.putChar(value);
    }
    
    public void write(final String value) {
        write(value, CharsetUtils.UTF8);
    }
    
    public void write(final String value, final Charset charset) {
        final byte[] bytes = value.getBytes(charset);
        write(bytes.length);
        write(bytes);
    }
    
    public void write(final double value) {
        flush(8);
        _buffer.putDouble(value);
    }
    
    public void write(final float value) {
        flush(4);
        _buffer.putFloat(value);
    }
    
    public void write(final short value) {
        flush(2);
        _buffer.putShort(value);
    }
    
    public void write(final boolean value) {
        flush(1);
        _buffer.put(value ? (byte) 1 : (byte) 0);
    }
    
    private void flush(final int mustAllowedSize) {
        final int allowedSize = _buffer.limit() - _buffer.position();
        if (allowedSize < mustAllowedSize) {
            flush();
        }
    }
    
    public void flush() {
        final byte[] bytes = _buffer.array();
        writeStream(bytes, 0, _buffer.position());
        _buffer.clear();
    }
    
    public void write(final byte[] bytes) {
        write(bytes, 0, bytes.length);
    }
    
    public void write(final byte[] bytes, final int offset, final int count) {
        final int bufferLimit = _buffer.limit();
        int pos = 0;
        while (pos < count) {
            final int blockSize = Math.min(bufferLimit, count - pos);
            flush(blockSize);
            _buffer.put(bytes, offset + pos, blockSize);
            pos += blockSize;
        }
    }
    
    protected void writeStream(final byte[] buffer, final int offset, final int count) {
        try {
            _stream.write(buffer, offset, count);
        } catch (final IOException e) {
            Log.e("BinaryWriter", "", e);
        }
    }
    
    // region Write Collections
    public <T> void WriteCollection(final Collection<T> collection, final WriteItemAction<T> writeItem) throws Exception {
        if (collection == null) {
            write(true);
            return;
        }
        write(false);
        if (collection instanceof ILocker) {
            synchronized(((ILocker)collection).Locker()) {
                write(collection.size());
                for (final T item : collection) {
                    writeItem.call(this, item);
                }
            }
        } else {
            write(collection.size());
            for (final T item : collection) {
                writeItem.call(this, item);
            }
        }
    }
    
    public <T> void WriteCollection(final T[] collection, final WriteItemAction<T> writeItem) throws Exception {
        if (collection == null) {
            write(true);
            return;
        }
        write(false);
        write(collection.length);
        for (final T item : collection) {
            writeItem.call(this, item);
        }
    }

    public void WriteCollection(final boolean[] collection, final WriteItemAction<Boolean> writeItem) throws Exception {
        if (collection == null) {
            write(true);
            return;
        }
        write(false);
        write(collection.length);
        for (final boolean item : collection) {
            writeItem.call(this, item);
        }
    }

    public void WriteCollection(final int[] collection, final WriteItemAction<Integer> writeItem) throws Exception {
        if (collection == null) {
            write(true);
            return;
        }
        write(false);
        write(collection.length);
        for (final int item : collection) {
            writeItem.call(this, item);
        }
    }

    public void WriteCollection(final double[] collection, final WriteItemAction<Double> writeItem) throws Exception {
        if (collection == null) {
            write(true);
            return;
        }
        write(false);
        write(collection.length);
        for (final double item : collection) {
            writeItem.call(this, item);
        }
    }

    public void WriteByteArray(final byte[] bytes) {
        if (bytes == null) {
            write(true);
            return;
        }
        write(false);
        write(bytes.length);
        write(bytes);
    }
    
    // private class WriteMapEntry<TKey, TValue> implements Action2<BinaryWriter, Map.Entry<TKey, TValue>> {
    // private final Action2<BinaryWriter, TKey> _writeKey;
    // private final Action2<BinaryWriter, TValue> _writeValue;
    //
    // public WriteMapEntry(final Action2<BinaryWriter, TKey> writeKey, final Action2<BinaryWriter, TValue> writeValue)
    // {
    // _writeKey = writeKey;
    // _writeValue = writeValue;
    // }
    //
    // @Override
    // public void invoke(final BinaryWriter writer, final Map.Entry<TKey, TValue> entry) {
    // _writeKey.invoke(writer, entry.getKey());
    // _writeValue.invoke(writer, entry.getValue());
    // }
    // }
    
    public <TKey, TValue> void WriteDictionary(final Map<TKey, TValue> dictionary,
        final WriteItemAction<TKey> writeKey, final WriteItemAction<TValue> writeValue) throws Exception {
        if (dictionary == null) {
            write(true);
            return;
        }
        write(false);
        if (dictionary instanceof ILocker) {
            synchronized(((ILocker)dictionary).Locker()) {
                write(dictionary.size());
                for (final Map.Entry<TKey, TValue> item : dictionary.entrySet()) {
                    writeKey.call(this, item.getKey());
                    writeValue.call(this, item.getValue());
                }
            }
        } else {
            write(dictionary.size());
            for (final Map.Entry<TKey, TValue> item : dictionary.entrySet()) {
                writeKey.call(this, item.getKey());
                writeValue.call(this, item.getValue());
            }
        }
    }
    
    public void write(final UUID value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value.getMostSignificantBits());
        write(value.getLeastSignificantBits());
    }
    
    @Deprecated
    public void write(final Parcelable value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        final Parcel parcel = Parcel.obtain();
        try {
            parcel.writeParcelable(value, 0);
            parcel.setDataPosition(0);
            final int availableSize = parcel.dataAvail();
            if (availableSize == 0) {
                write(true);
                return;
            }
            final byte[] buffer = parcel.marshall();
            WriteByteArray(buffer);
        } finally {
            parcel.recycle();
        }
    }
    
    // endregion
    
    // region Nullable
    /** Write Nullable */
    public void WriteNullable(final IStreamSerializable value) throws Exception {
        final boolean hasValue = value != null;
        write(hasValue);
        if (hasValue) {
            value.Serialize(this);
        }
    }

    public <T> void WriteNullable(final T value, final WriteItemAction<T> writeItem) throws Exception {
        final boolean hasValue = value != null;
        write(hasValue);
        if (hasValue) {
            writeItem.call(this, value);
        }
    }
    
    public void WriteNullable(final String value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Integer value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Double value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Float value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Byte value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Short value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Long value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Boolean value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }
    
    public void WriteNullable(final Character value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value);
    }

    public void WriteNullable(final TimeSpan value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value.Ticks);
    }

    public void WriteNullable(final DateTime value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value.Ticks);
    }

    public void WriteNullable(final URI value) {
        if (value == null) {
            write(true);
            return;
        }
        write(false);
        write(value.toString());
    }

    // endregion
    
    // region Writers
    
    public static final WriteItemAction<UUID> UUIDWriter = (writer, value) -> writer.write(value);
    
    public static final WriteItemAction<Long> LongWriter = (writer, value) -> writer.write(value);
    
    public static final WriteItemAction<Integer> IntegerWriter = (writer, value) -> writer.write(value);
    
    public static final WriteItemAction<String> StringWriter = (writer, value) -> writer.WriteNullable(value);
    
    // endregion
}
