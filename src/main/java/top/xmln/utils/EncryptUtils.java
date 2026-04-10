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
        if ("ECC".equals(algorithm)) {
            algorithm = "EC";
        }
        try {
            PrintUtils.infoFormat("正在使用%s算法生成非对称密钥对...", algorithm);
            // 创建密钥对
            KeyPair keyPair = KeyPairGenerator.getInstance(algorithm).generateKeyPair();
            byte[] publicKey = keyPair.getPublic().getEncoded(); // 公钥
            byte[] privateKey = keyPair.getPrivate().getEncoded(); // 私钥

            // 编码为Base64字符串
            AsymmetricResult result = new AsymmetricResult();
            result.publicKey = base64EncodeBytes(publicKey);
            result.privateKey = base64EncodeBytes(privateKey);
            return result;
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.errorFormat("加密算法不存在：%s", e.getMessage());
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
            PrintUtils.infoFormat("正在使用%s算法非对称加密...", algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(algorithm, key));
            return base64EncodeBytes(cipher.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.errorFormat("加密算法不存在：%s", e.getMessage());
            return null;
        } catch (NoSuchPaddingException e) {
            PrintUtils.errorFormat("加密填充异常：%s", e.getMessage());
            return null;
        } catch (InvalidKeyException e) {
            PrintUtils.errorFormat("密钥异常：%s", e.getMessage());
            return null;
        } catch (IllegalBlockSizeException e) {
            PrintUtils.errorFormat("数据块大小异常：%s", e.getMessage());
            return null;
        } catch (BadPaddingException e) {
            PrintUtils.errorFormat("填充异常：%s", e.getMessage());
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
            PrintUtils.infoFormat("正在使用%s算法非对称解密...", algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, getKey(algorithm, key));
            return new String(cipher.doFinal(base64DecodeBytes(encryptedData)));
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.errorFormat("解密算法不存在：%s", e.getMessage());
            return null;
        } catch (NoSuchPaddingException e) {
            PrintUtils.errorFormat("解密填充异常：%s", e.getMessage());
            return null;
        } catch (InvalidKeyException e) {
            PrintUtils.errorFormat("密钥异常：%s", e.getMessage());
            return null;
        } catch (IllegalBlockSizeException e) {
            PrintUtils.errorFormat("数据块大小异常：%s", e.getMessage());
            return null;
        } catch (BadPaddingException e) {
            PrintUtils.errorFormat("填充异常：%s", e.getMessage());
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
            PrintUtils.errorFormat("加密算法不存在：%s", e.getMessage());
            return null;
        } catch (InvalidKeySpecException e) {
            PrintUtils.errorFormat("密钥规格异常：%s", e.getMessage());
            return null;
        }
    }

    /**
     * 对数据进行签名
     *
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥字符串
     * @param data          数据字符串
     * @return 签名字符串
     */
    public static String sign(String signAlgorithm, String privateKey, String data) {
        try {
            PrintUtils.infoFormat("正在使用%s算法签名...", signAlgorithm);
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initSign(getPrivateKey(signAlgorithm.split("with")[1], privateKey));
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] sign = signature.sign();
            return base64EncodeBytes(sign);
        } catch (Exception e) {
            PrintUtils.errorFormat("签名失败：%s", e.getMessage());
            return null;
        }
    }

    /**
     * 对数据进行签名验签
     *
     * @param signAlgorithm 签名算法
     * @param publicKey     公钥字符串
     * @param data          数据字符串
     * @param sign          签名字符串
     * @return 验签结果
     */
    public static boolean verify(String signAlgorithm, String publicKey, String data, String sign) {
        try {
            PrintUtils.infoFormat("正在使用%s算法验签...", signAlgorithm);
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initVerify(getPublicKey(signAlgorithm.split("with")[1], publicKey));
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            boolean verify = signature.verify(base64DecodeBytes(sign));
            PrintUtils.infoFormat("签名结果：%s", verify ? "正确" : "错误");
            return verify;
        } catch (Exception e) {
            PrintUtils.errorFormat("验签失败：%s", e.getMessage());
            return false;
        }
    }

    /**
     * 获取密钥对中的私钥
     *
     * @param algorithm 密钥算法
     * @param base64Key 密钥字符串
     * @return 私钥
     */
    private static PrivateKey getPrivateKey(String algorithm, String base64Key) throws Exception {
        return KeyFactory.getInstance(algorithm).generatePrivate(new PKCS8EncodedKeySpec(base64DecodeBytes(base64Key)));
    }

    /**
     * 获取密钥对中的公钥
     *
     * @param algorithm 密钥算法
     * @param base64Key 密钥字符串
     * @return 公钥
     */
    private static PublicKey getPublicKey(String algorithm, String base64Key) throws Exception {
        return KeyFactory.getInstance(algorithm).generatePublic(new X509EncodedKeySpec(base64DecodeBytes(base64Key)));
    }

    /**
     * 返回数据摘要
     *
     * @param digestAlgorithm 摘要算法
     * @param data            数据
     * @return 摘要
     */
    public static String digest(String digestAlgorithm, String data) {
        PrintUtils.infoFormat("正在使用%s算法计算摘要...", digestAlgorithm);
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            digest.update(data.getBytes());
            byte[] bytes = digest.digest();
            return base64EncodeBytes(bytes);
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
