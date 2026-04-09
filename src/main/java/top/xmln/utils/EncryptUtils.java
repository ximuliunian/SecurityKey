package top.xmln.utils;

import lombok.Getter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 加密工具类
 */
public class EncryptUtils {

    /**
     * 非对称密钥对生成
     *
     * @param algorithm 密钥算法
     * @return 算法结果
     */
    public static AsymmetricResult asymmetric(String algorithm) {
        try {
            PrintUtils.info(String.format("正在使用%s算法生成非对称密钥对...", algorithm));
            // 创建密钥对
            KeyPair keyPair = KeyPairGenerator.getInstance(algorithm).generateKeyPair();
            byte[] publicKey = keyPair.getPublic().getEncoded(); // 公钥
            byte[] privateKey = keyPair.getPrivate().getEncoded(); // 私钥

            // 编码为Base64字符串
            AsymmetricResult result = new AsymmetricResult();
            result.publicKey = base64EncodeBytes(publicKey);
            result.privateKey = base64EncodeBytes(privateKey);
            PrintUtils.success("密钥对生成成功");
            return result;
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.error("加密算法不存在");
            return null;
        }
    }

    /**
     * 非对称加密
     *
     * @param algorithm 密钥算法
     * @param key       密钥
     * @param data      数据
     * @return 加密后的字符串
     */
    public static String asymmetricEncrypt(String algorithm, String key, String data) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(algorithm, key));
            return base64EncodeBytes(cipher.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.error("加密算法不存在");
            return null;
        } catch (NoSuchPaddingException e) {
            PrintUtils.error("加密填充异常");
            return null;
        } catch (InvalidKeyException e) {
            PrintUtils.error("密钥异常");
            return null;
        } catch (IllegalBlockSizeException e) {
            PrintUtils.error("数据块大小异常");
            return null;
        } catch (BadPaddingException e) {
            PrintUtils.error("填充异常");
            return null;
        }
    }

    /**
     * 非对称解密
     *
     * @param algorithm     密钥算法
     * @param key           密钥
     * @param encryptedData 加密后的字符串
     * @return 解密后的字符串
     */
    public static String asymmetricDecrypt(String algorithm, String key, String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, getKey(algorithm, key));
            return new String(cipher.doFinal(base64DecodeBytes(encryptedData)));
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.error("解密算法不存在");
            return null;
        } catch (NoSuchPaddingException e) {
            PrintUtils.error("解密填充异常");
            return null;
        } catch (InvalidKeyException e) {
            PrintUtils.error("密钥异常");
            return null;
        } catch (IllegalBlockSizeException e) {
            PrintUtils.error("数据块大小异常");
            return null;
        } catch (BadPaddingException e) {
            PrintUtils.error("填充异常");
            return null;
        }
    }

    /**
     * 获取密钥
     *
     * @param algorithm 密钥算法
     * @param base64Key 密钥字符串
     * @return 密钥
     */
    private static Key getKey(String algorithm, String base64Key) {
        try {
            byte[] bytes = base64DecodeBytes(base64Key);
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            try {
                return factory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
            } catch (Exception e) {
                return factory.generatePublic(new X509EncodedKeySpec(bytes));
            }
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.error("加密算法不存在");
            return null;
        } catch (InvalidKeySpecException e) {
            PrintUtils.error("密钥规格异常");
            return null;
        }
    }


    /**
     * 返回数据摘要
     *
     * @param digestAlgorithm 摘要算法
     * @param data            数据
     * @return 摘要
     */
    public static String digest(String digestAlgorithm, String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            digest.update(data.getBytes());
            return base64EncodeBytes(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.error("摘要算法不存在");
            return null;
        }
    }


    /**
     * Base64编码
     *
     * @param data 字节数组
     * @return 编码后的字符串
     */
    public static String base64EncodeBytes(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64解码
     *
     * @param data 编码后的字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64DecodeBytes(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * Base64编码
     *
     * @param data 字符串
     * @return 编码后的字符串
     */
    public static String base64EncodeStr(String data) {
        return base64EncodeBytes(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64解码
     *
     * @param data 编码后的字符串
     * @return 解码后的字符串
     */
    public static String base64DecodeStr(String data) {
        return new String(base64DecodeBytes(data), StandardCharsets.UTF_8);
    }

    /**
     * 非对称加密结果
     */
    @Getter
    public static class AsymmetricResult {
        /**
         * 公钥
         */
        private String publicKey;

        /**
         * 私钥
         */
        private String privateKey;
    }
}
