package com.github.nikolaymakhonin.utils.serialization;

import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.utils.RefParam;
import com.github.nikolaymakhonin.utils.strings.CharsetUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class StreamSerializerUtils {
    private static final int currentVersion = 3;
    
    // MAX_COMPRESSION_LEVEL
    public static boolean SaveToByteArray(final IStreamSerializable obj, final RefParam<byte[]> buffer) {
        return SaveToByteArray(obj, buffer, CompressMethod.None, 0, null, null, null);
    }
    
    public static boolean SaveToByteArray(final IStreamSerializable obj, final RefParam<byte[]> buffer,
        final CompressMethod compressMethod, final int compressionLevel, final String cryptKey, final String cryptIV,
        final Charset cryptEncoding) {
        try {
            final ByteArrayOutputStream memStream = new ByteArrayOutputStream();
            if (SaveToStream(obj, memStream, compressMethod, compressionLevel, cryptKey, cryptIV, cryptEncoding)) {
                buffer.value = memStream.toByteArray();
                memStream.close();
                return true;
            }
            buffer.value = null;
            memStream.close();
            return false;
        } catch (final IOException e) {
            Log.e("StreamSerializerUtils", "", e);
            buffer.value = null;
            return false;
        }
    }
    
    // public static boolean SaveToFile(final IStreamSerializable obj, final String filePath, final CompressMethod
    // compressMethod = CompressMethod.None, int compressionLevel = mika.utils.Serialization.MAX_COMPRESSION_LEVEL,
    // String cryptKey = null, String cryptIV = null, Charset cryptEncoding = null)
    // {
    // using (final Stream fileStream = FileUtils.FileOpenWrite(filePath))
    // {
    // return SaveToStream(obj, fileStream, compressMethod, compressionLevel, cryptKey, cryptIV, cryptEncoding);
    // }
    // }
    
    // CompressionUtils.MAX_COMPRESSION_LEVEL
    public static  boolean SaveToStream(final IStreamSerializable obj, final OutputStream stream, final CompressMethod compressMethod, final int compressionLevel, final String cryptKey , final String cryptIV, final Charset cryptEncoding)
    {
        try
        {
            final BinaryWriter writer = new BinaryWriter(stream);
            writer.write(currentVersion);
            switch (currentVersion)
            {
                case 3:
                    writer.write(compressMethod.ordinal());
                    writer.write(compressionLevel);
                    final boolean encrypt = cryptKey != null && cryptIV != null;
                    writer.write(encrypt);
                    
                    if (compressMethod == CompressMethod.None && !encrypt)
                    {
                        obj.Serialize(writer);
                        writer.flush();
                    }
                    else
                    {
                        writer.flush();
                        final ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
                        final BinaryWriter writer2 = new BinaryWriter(bufferOutputStream);
                        obj.Serialize(writer2);
                        writer2.flush();
                        ByteArrayInputStream bufferInputStream = new ByteArrayInputStream(bufferOutputStream.toByteArray());
                        if (compressMethod != CompressMethod.None)
                        {
                            if (encrypt)
                            {
                                final ByteArrayOutputStream packStream = new ByteArrayOutputStream();
                                final long result = CompressionUtils.Pack(bufferInputStream, packStream, compressMethod, compressionLevel);
                                if (result < 0) {
                                    return false;
                                }
                                bufferInputStream = new ByteArrayInputStream(packStream.toByteArray());
                            }
                            else
                            {
                                final long result = CompressionUtils.Pack(bufferInputStream, stream, compressMethod, compressionLevel);
                                if (result < 0) {
                                    return false;
                                }
                            }
                        }
                        if (encrypt)
                        {
                            final boolean result = CryptUtils.Crypt(CryptMode.Encrypt, bufferInputStream, stream, cryptEncoding != null ? cryptEncoding : CharsetUtils.UTF8, cryptKey, cryptIV, false);
                            if (!result) {
                                return false;
                            }
                        }
                    }
                    break;
            }
        }
        catch (final Exception exception)
        {
            Log.e("UnknownLogTag", "", exception);
            return false;
        }

        return true;
    }

    public static boolean LoadFromByteArray(final IStreamSerializable obj, final byte[] buffer) {
        return LoadFromByteArray(obj, buffer, null, null);
    }
    
    /** @param cryptKey
     *            - maxUsedLength = 24 bytes
     * @param cryptIV
     *            - maxUsedLength = 8 bytes */
    public static boolean LoadFromByteArray(final IStreamSerializable obj, final byte[] buffer, final String cryptKey,
        final String cryptIV) {
        try {
            if (buffer == null) {
                Log.e("UnknownLogTag", "buffer == null");
                return false;
            }
            final ByteArrayInputStream memStream = new ByteArrayInputStream(buffer);
            final boolean result = loadFromStream(obj, memStream, cryptKey, cryptIV, null);
            memStream.close();
            return result;
        } catch (final IOException e) {
            Log.e("StreamSerializerUtils", "", e);
            return false;
        }
    }
    
    // public static boolean LoadFromFile(final IStreamSerializable obj, final String filePath)
    // {
    // return LoadFromFile(obj, filePath, null, null);
    // }
    //
    // public static boolean LoadFromFile(final IStreamSerializable obj, final String filePath, final String cryptKey =
    // null, String cryptIV = null)
    // {
    // using (final Stream fileStream = FileUtils.FileOpenRead(filePath))
    // {
    // return loadFromStream( obj, fileStream, null);
    // }
    // }
    
    public static boolean LoadFromStream(final IStreamSerializable obj, final ByteArrayInputStream stream,
        final String cryptKey, final String cryptIV, final Charset cryptEncoding) {
        return loadFromStream(obj, stream, cryptKey, cryptIV, cryptEncoding);
    }
    
    public static boolean LoadFromStream(final IStreamSerializable obj, final ByteArrayInputStream stream) {
        return loadFromStream(obj, stream, null, null, null);
    }
    
    private static boolean loadFromStream( final IStreamSerializable obj, InputStream stream, final String cryptKey, final String cryptIV, final Charset cryptEncoding)
    {
        try
        {
            BinaryReader reader = new BinaryReader(stream);
            final int version = reader.readInt();
            CompressMethod  compressMethod;
            switch (version)
            {
                case 3:
                    compressMethod = CompressMethod.values()[reader.readInt()];
                    final int compressionLevel = reader.readInt();
                    final boolean encrypt = reader.readBoolean();
                    if (compressMethod != CompressMethod.None || encrypt)
                    {
                        if (encrypt)
                        {
                            final ByteArrayOutputStream decryptStream = new ByteArrayOutputStream();
                            final boolean result = CryptUtils.Crypt(CryptMode.Decrypt, stream, decryptStream, cryptEncoding != null ? cryptEncoding : CharsetUtils.UTF8, cryptKey, cryptIV, false);
                            if (!result) {
                                return false;
                            }
                            stream = new ByteArrayInputStream(decryptStream.toByteArray());
                        }
                        if (compressMethod != CompressMethod.None)
                        {
                            final ByteArrayOutputStream unPackStream = new ByteArrayOutputStream();
                            final long result = CompressionUtils.UnPack(stream, unPackStream, compressMethod, compressionLevel);
                            if (result < 0) {
                                return false;
                            }
                            stream = new ByteArrayInputStream(unPackStream.toByteArray());
                        }
                        reader = new BinaryReader(stream);
                    }
                    obj.DeSerialize(reader);
                    break;
                default:
                    Log.e("UnknownLogTag", "DeSerialize unavailable version: " + version);
                    return false;
            }
        }
        catch (final Exception exception)
        {
            Log.e("UnknownLogTag", "", exception);
            return false;
        }

        return true;
    }
}
