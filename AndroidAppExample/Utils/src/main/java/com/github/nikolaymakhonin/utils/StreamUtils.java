package com.github.nikolaymakhonin.utils;

import com.github.nikolaymakhonin.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class StreamUtils
{
    /** Копирует данные из потока в поток. Чтобы прочитать их потом из Dst, нужно делать так: Dst.setPosition( 0

@param src
Источник

@param dst
Приемник

*/
    public static  long TryStreamCopyTo(final InputStream src, final OutputStream dst)
    {
         return TryStreamCopyTo(src, dst, 4096);
    }

    public static  long TryStreamCopyTo(final InputStream src, final OutputStream dst, int bufferSize)
    {
        try
        {
//            if (src.getBaseStream().CanSeek)
//            {
//                if (bufferSize <= 0 || bufferSize > src.getBaseStream().length && src.getBaseStream().length > 0) {
//                    bufferSize = (int) src.getBaseStream().length;
//                }
//            }
//            else
            if (bufferSize <= 0) {
                bufferSize = 4096;
            }
            int cnt;
            final byte[] buffer = new byte[bufferSize];
            int copiedBytes = 0;
            while ((cnt = src.read(buffer, 0, buffer.length)) > 0)
            {
                dst.write(buffer, 0, cnt);
                copiedBytes += cnt;
            }
            return copiedBytes;
        }
        catch (final Exception e)
        {
            return -1;
        }
    }

    public static  long StreamCopyTo(final InputStream reader, final OutputStream writer, final long count, final int bufferSize)
    {
         return StreamCopyTo(reader, Arrays.asList(new OutputStream[] {writer}), count, bufferSize);
    }

    public static  long StreamCopyTo(final InputStream reader, final Iterable<OutputStream> writers, final long count, int bufferSize)
    {
        if (count <= 0) {
            return 0;
        }
        if (bufferSize > count) {
            bufferSize = (int)count;
        }
        final byte[] buffer = new byte[bufferSize];
        long totalRead = 0;
        try {
            int cnt = totalRead + buffer.length > count ? (int)(count - totalRead > Integer.MAX_VALUE ? Integer.MAX_VALUE : count - totalRead) : buffer.length;
            int readCount = reader.read(buffer, 0, cnt);
            while (readCount > 0)
            {
                totalRead += readCount;
                for (final OutputStream writer : writers)
                {
                    writer.write(buffer, 0, readCount);
                }
                if (totalRead >= count) {
                    break;
                }
                cnt = totalRead + buffer.length > count ? (int)(count - totalRead > Integer.MAX_VALUE ? Integer.MAX_VALUE : count - totalRead) : buffer.length;
                readCount = reader.read(buffer, 0, cnt);
            }
        } catch (final IOException e) {
            Log.e("StreamUtils", "", e);
        }
        return totalRead;
    }
}
