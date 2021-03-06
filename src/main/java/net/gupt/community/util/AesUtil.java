package net.gupt.community.util;

import net.gupt.community.entity.AesTokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <h3>gupt-community</h3>
 * <p>Aes加解密工具类</p>
 *
 * @author : Cui
 * @date : 2019-07-31 08:59
 **/
@Component
public class AesUtil {

    @Autowired
    private AesTokenConfig aesTokenConfig;
    private static AesTokenConfig config;

    private static final String ALGORITHM = "AES";
    /**
     * 算法/模式/补码方式
     */
    private static final String ALGORITHM_PROVIDER = "AES/CBC/PKCS5Padding";

    @PostConstruct
    public void init() {
        config = this.aesTokenConfig;
    }

    private static IvParameterSpec getIv() {
        return new IvParameterSpec(config.getAesTokenIv().getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] encrypt(String src) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SecretKey secretKey = new SecretKeySpec(config.getAesTokenKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
        IvParameterSpec ivParameterSpec = getIv();
        Cipher cipher = Cipher.getInstance(ALGORITHM_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        return cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decrypt(String src) throws Exception {
        SecretKey secretKey = new SecretKeySpec(config.getAesTokenKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);

        IvParameterSpec ivParameterSpec = getIv();
        Cipher cipher = Cipher.getInstance(ALGORITHM_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] hexBytes = hexStringToBytes(src);
        return cipher.doFinal(hexBytes);
    }

    /**
     * 将byte转换为16进制字符串
     *
     * @param src byte数组
     * @return 16进制字符串
     */
    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (byte b : src) {
            int v = b & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串装换为byte数组
     *
     * @param hexString 16进制字符串
     * @return byte数组
     */
    private static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
