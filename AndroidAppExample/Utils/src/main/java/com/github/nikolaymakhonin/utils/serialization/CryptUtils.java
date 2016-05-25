package com.github.nikolaymakhonin.utils.serialization;

import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.utils.StreamUtils;
import com.github.nikolaymakhonin.utils.strings.CharsetUtils;
import com.github.nikolaymakhonin.utils.strings.StringUtilsExt;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("SpellCheckingInspection")
public class CryptUtils {
    
    private static MessageDigest _md5;
    
    public static String MD5(final String md5) {
        try {
            if (_md5 == null) {
                _md5 = MessageDigest.getInstance("MD5");
            }
            final byte[]        array = _md5.digest(md5.getBytes());
            final StringBuilder sb    = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (final NoSuchAlgorithmException e) {
            Log.e("CryptUtils", "MD5 algorithm not found");
        }
        return null;
    }
    
    /** Генерирует ключ для функции шифрования */
    // public static byte[] GenerateCryptKey(int... Keys)
    // {
    // if ((Keys == null) || (Keys.length == 0)) Keys = new int[1] { 0 };
    // int KeySize = cryptoProvider.getLegalKeySizes().get(0).MaxSize / 8;
    // byte[] Key = new byte.get(KeySize);
    // byte[] key = new byte.get(KeySize);
    // Random[] Rnds = new Random.get(Keys.length);
    // for (int i = 0; i < Keys.length; i++) Rnds.set(i, new Random(Keys.get(i)));
    
    // do
    // {
    // for (int i = 0; i < Rnds.length; i++)
    // {
    // Rnds.get(i).NextBytes(key);
    // for(int j=0; j<key.length; j++) Key.get(j)+= key.get(j);
    // }
    // //cryptoProvider.GenerateKey();
    // } while (TripleDES.IsWeakKey(Key));
    
    // return Key;
    // }
    // private static final TripleDESCryptoServiceProvider cryptoProvider = new TripleDESCryptoServiceProvider();
    
    // static int[] stringToIntArray(String text)
    // {
    // var buffer = CharsetUtils.ANSI.GetBytes(text);
    // int len = buffer.length;
    // var result = new int.get(len/4 + (len%4 == 0 ? 0 : 1));
    // int value = 0;
    // for (int i = 0; i < len; i++)
    // {
    // int n = i%4;
    // value = (value << n*8) | buffer.get(i);
    // if (n == 3)
    // {
    // result.set(i / 4, value);
    // value = 0;
    // }
    // }
    // return result;
    // }
    
    // private static byte[] generateKeyFromString(final String key, final int countBytes, final Charset encoding) {
    // final byte[] keyBytes = new byte[countBytes];
    // final byte[] keyBytes = StringUtils.getBytes(key, encoding);
    // System.arraycopy(keyBytes, 0, keyBytes, 0, countBytes);
    // return keyBytes;
    // }
    
    private static byte[] generateKeyFromString(final String key, final int countBytes, Charset encoding) {
        final byte[] keyBytes = new byte[countBytes];
        if (encoding == null) {
            encoding = CharsetUtils.UTF8;
        }
        final byte[] stringBytes = key.getBytes(encoding);
        if (stringBytes.length < countBytes) {
            System.arraycopy(stringBytes, 0, keyBytes, 0, stringBytes.length);
            for (int i = stringBytes.length; i < countBytes; i++) {
                keyBytes[i] = (byte) (i + 1);
            }
        } else {
            System.arraycopy(stringBytes, 0, keyBytes, 0, countBytes);
        }
        return keyBytes;
    }
    
    public static String CryptANSI(final CryptMode cryptMode, final String input, final String cryptKey, final String iv) {
        return CryptANSI(cryptMode, input, cryptKey, iv, false);
    }
    
    public static String CryptANSI(final CryptMode cryptMode, final String input, final String cryptKey,
        final String iv, final boolean disableLog) {
        return CryptANSI(cryptMode, input, CharsetUtils.UTF8, cryptKey, iv, disableLog);
    }
    
    public static String CryptANSI(final CryptMode cryptMode, final String input, final Charset encoding,
        final String cryptKey, final String iv, final boolean disableLog) {
        if (cryptMode == CryptMode.Encrypt) { return new String(
            Crypt(cryptMode, input.getBytes(encoding), encoding, cryptKey, iv, disableLog),
            CharsetUtils.UTF8); }
        String result = new String(
            Crypt(cryptMode, input.getBytes(CharsetUtils.UTF8), encoding, cryptKey, iv, disableLog),
            encoding);
        result = StringUtils.stripEnd(result, "\0");
        return result;
    }
    
    public static String Crypt(final CryptMode cryptMode, final String input, final String cryptKey, final String iv) {
        return Crypt(cryptMode, input, cryptKey, iv, false);
    }
    
    public static String Crypt(final CryptMode cryptMode, final String input, final String cryptKey, final String iv,
        final boolean disableLog) {
        return Crypt(cryptMode, input, CharsetUtils.UTF8, cryptKey, iv, disableLog);
    }
    
    public static String Crypt(final CryptMode cryptMode, final String input, final Charset encoding,
        final String cryptKey, final String iv, final boolean disableLog) {
        if (cryptMode == CryptMode.Encrypt) { return StringUtilsExt.toBase64(Crypt(cryptMode,
            input.getBytes(encoding), encoding, cryptKey, iv, disableLog)); }
        String result = new String(
            Crypt(cryptMode, StringUtilsExt.fromBase64(input), encoding, cryptKey, iv, disableLog), encoding);
        result = StringUtils.stripEnd(result, "\0");
        return result;
    }
    
    public static byte[] Crypt(final CryptMode cryptMode, final byte[] input, final Charset encoding,
        final String cryptKey, final String iv, final boolean disableLog) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream(input.length);
        if (Crypt(cryptMode, new ByteArrayInputStream(input), output, encoding, cryptKey, iv, disableLog)) { return output
            .toByteArray(); }
        return new byte[0];
    }
    
    static final int keyMinSize  = 0x80;
    static final int keyMaxSize  = 0xc0;
    static       int keySkipSize = 0x40;
    static final int IVSize      = 8;
    
    public static boolean Crypt(final CryptMode cryptMode, final InputStream input, final OutputStream output,
        final Charset encoding, final String cryptKey, final String iv, final boolean disableLog) {
        
        final byte[] cryptKeyBytes = generateKeyFromString(cryptKey, keyMaxSize / 8, encoding);
        final byte[] ivBytes = generateKeyFromString(iv, IVSize, encoding);
        return Crypt(cryptMode, input, output, cryptKeyBytes, ivBytes, disableLog);
    }
    
    // region TrippleDes.IsWeakKey
    private static byte[] FixupKeyParity(final byte[] key) {
        final byte[] buffer = new byte[key.length];
        for (int i = 0; i < key.length; i++) {
            buffer[i] = (byte) (key[i] & 0xfe);
            final byte num2 = (byte) ((buffer[i] & 15) ^ (buffer[i] >> 4));
            final byte num3 = (byte) ((num2 & 3) ^ (num2 >> 2));
            if (((byte) ((num3 & 1) ^ (num3 >> 1))) == 0) {
                buffer[i] = (byte) (buffer[i] | 1);
            }
        }
        return buffer;
    }
    
    private static boolean IsLegalKeySize(final byte[] rgbKey) {
        return !((rgbKey == null) || ((rgbKey.length != 0x10) && (rgbKey.length != 0x18)));
    }
    
    private static boolean EqualBytes(final byte[] rgbKey, final int start1, final int start2, final int count) {
        for (int i = 0; i < count; i++) {
            if (rgbKey[start1 + i] != rgbKey[start2 + i]) { return false; }
        }
        return true;
    }
    
    public static boolean IsWeakKey(final byte[] rgbKey) {
        final byte[] buffer = FixupKeyParity(rgbKey);
        return (EqualBytes(buffer, 0, 8, 8) || ((buffer.length == 0x18) && EqualBytes(buffer, 8, 0x10, 8)));
    }
    
    // endegion
    
    // http://stackoverflow.com/questions/4398874/encryption-between-android-and-c-sharp
    // https://code.google.com/p/anandsoftware-security-plugin/source/browse/trunk/as-secure/src/com/anandsoftware/security/impl/TrippleDES.java?spec=svn8&r=8
    // http://www.java2s.com/Tutorial/Java/0490__Security/UsingCipherOutputStream.htm
    // http://www.java2s.com/Tutorial/Java/0490__Security/UsingCipherInputStream.htm
    
    private static Cipher _tdes;

    private static final Object _locker = new Object();
    
    public static boolean Crypt(final CryptMode cryptMode, final InputStream input, final OutputStream output,
        final byte[] cryptKeyBytes, final byte[] ivBytes, final boolean disableLog) {
        // if (input.length == 0) {
        // return false;
        // }
        synchronized (_locker) {
            try {
                if (IsWeakKey(cryptKeyBytes)) {
                    cryptKeyBytes[cryptKeyBytes.length - 1] = (byte) (cryptKeyBytes[cryptKeyBytes.length - 1] ^ 1);
                }
                
                if (_tdes == null) {
                    _tdes = Cipher.getInstance("DESede/CBC/NoPadding");
                }
                final SecretKeySpec key = new SecretKeySpec(cryptKeyBytes, "DESede");
                final IvParameterSpec iv = new IvParameterSpec(ivBytes);
                switch (cryptMode) {
                    case Encrypt:
                        _tdes.init(Cipher.ENCRYPT_MODE, key, iv);
                        final CipherOutputStream outputCryptStream = new CipherOutputStream(output, _tdes);
                        StreamUtils.StreamCopyTo(input, outputCryptStream, Integer.MAX_VALUE, 4096);
                        break;
                    case Decrypt:
                        _tdes.init(Cipher.DECRYPT_MODE, key, iv);
                        final CipherInputStream inputCryptStream = new CipherInputStream(input, _tdes);
                        StreamUtils.StreamCopyTo(inputCryptStream, output, Integer.MAX_VALUE, 4096);
                        break;
                }
                return true;
            } catch (final Exception exception) {
                if (!disableLog) {
                    Log.e("UnknownLogTag", "Decrypt error", exception);
                }
                return false;
            }
        }
    }
}
