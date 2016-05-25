package com.github.nikolaymakhonin.utils.serialization;

import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CompressionUtils {
    public static final int MAX_COMPRESSION_LEVEL = 9;
    
    public static long Pack(final InputStream input, final OutputStream output, final CompressMethod compressMethod,
        final int compressionLevel) {
        return PackUnPack(input, output, compressMethod, compressionLevel, CompressionMode.Compress);
    }
    
    public static long UnPack(final InputStream input, final OutputStream output, final CompressMethod compressMethod,
        final int compressionLevel) {
        return PackUnPack(input, output, compressMethod, compressionLevel, CompressionMode.Decompress);
    }
    
    public static long Pack(final InputStream input, final OutputStream output, final CompressMethod compressMethod) {
        return PackUnPack(input, output, compressMethod, MAX_COMPRESSION_LEVEL, CompressionMode.Compress);
    }
    
    public static long UnPack(final InputStream input, final OutputStream output, final CompressMethod compressMethod) {
        return PackUnPack(input, output, compressMethod, MAX_COMPRESSION_LEVEL, CompressionMode.Decompress);
    }
    
    private static long PackUnPack(final InputStream input, final OutputStream output,
        final CompressMethod compressMethod, final int compressionLevel, final CompressionMode compressionMode) {
        long result;
        if (input == null || output == null) { return -1; }
        try {
            switch (compressMethod) {
                case Zip:
                    if (compressionMode == CompressionMode.Compress) {
                        final ZipOutputStream zipOutputStream = new ZipOutputStream(output);
                        zipOutputStream.setLevel(compressionLevel);
                        zipOutputStream.putNextEntry(new ZipEntry("Zip"));
                        result = StreamUtils.TryStreamCopyTo(input, zipOutputStream);
                        zipOutputStream.finish();
                    } else {
                        final ZipInputStream zipInputStream = new ZipInputStream(input);
                        final ZipEntry entry = zipInputStream.getNextEntry();
                        if (entry == null) {
                            result = -1;
                        } else {
                            result = StreamUtils.TryStreamCopyTo(zipInputStream, output);
                        }
                        zipInputStream.closeEntry();
                        zipInputStream.close();
                    }
                    break;
                default:
                    result = StreamUtils.TryStreamCopyTo(input, output);
                    break;
            }
        } catch (final IOException e) {
            Log.e("CompressionUtils", "", e);
            result = -1;
        }
        return result;
    }
}
