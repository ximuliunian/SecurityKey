package top.xmln.cli.gen;

import top.xmln.option.*;
import top.xmln.utils.EncryptUtils;
import top.xmln.utils.FileUtils;
import top.xmln.utils.JsonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenerateCert implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 过期时间（毫秒时间戳）
        optionsParser.add(new OptionsItem("expire", Arrays.asList("-e", "--expire"), Type.Number, null, "证书过期时间（毫秒时间戳）"));
        // 签名私钥路径
        optionsParser.add(new OptionsItem("signPath", Arrays.asList("-sp", "--signPath"), Type.String, "./output/private.key", "签名私钥路径"));
        // 签名密钥算法
        optionsParser.add(new OptionsItem("signAlgorithm", Arrays.asList("-sa", "--signAlgorithm"), Type.String, "RSA", "签名密钥算法（RSA、ECC）"));
        // 加密算法
        optionsParser.add(new OptionsItem("algorithm", Arrays.asList("-a", "--algorithm"), Type.String, "RSA", "加密算法（RSA、ECC）"));
        // 保存位置
        optionsParser.add(new OptionsItem("save", Arrays.asList("-s", "--save"), Type.String, "./output/", "保存带证书的密钥对目录（带/）"));
        // 证书名称
        optionsParser.add(new OptionsItem("name", Arrays.asList("-n", "--name"), Type.String, "xmln", "证书名称"));
        // 摘要算法
        optionsParser.add(new OptionsItem("digestAlgorithm", Arrays.asList("-da", "--digestAlgorithm"), Type.String, "SHA-256", "摘要算法（MD5、SHA1、SHA256、SHA512）"));
    }

    @Override
    public void run(Map<String, Option> options) {
        Map<String, String> cert = new HashMap<>();
        // 证书ID
        cert.put("id", UUID.randomUUID().toString());
        // 证书名称
        String name = (String) options.get("name").value();
        cert.put("name", name);
        // 签发时间
        cert.put("signTime", String.valueOf(System.currentTimeMillis()));
        // 加密算法
        String algorithm = (String) options.get("algorithm").value();
        cert.put("algorithm", algorithm);
        // 过期时间（毫秒时间戳）
        cert.put("expireTime", options.get("expire").value().toString());
        // 摘要算法
        String digestAlgorithm = (String) options.get("digestAlgorithm").value();
        cert.put("digestAlgorithm", digestAlgorithm);
        // 签名密钥算法
        String signAlgorithm = (String) options.get("signAlgorithm").value();
        cert.put("signAlgorithm", signAlgorithm);

        // 生成密钥对
        EncryptUtils.AsymmetricResult result = EncryptUtils.asymmetric(algorithm);
        if (result == null) {
            return;
        }

        // 公钥
        cert.put("publicKey", result.getPublicKey());

        // 获取证书摘要
        String json = JsonUtils.toJson(cert);
        String digest = EncryptUtils.digest(digestAlgorithm, json);
        if (digest == null) {
            return;
        }

        // 签名
        String signPath = (String) options.get("signPath").value();
        String privateKey = FileUtils.readFile(signPath);
        String sign = EncryptUtils.asymmetricEncrypt(signAlgorithm, privateKey, digest);
        if (sign == null) {
            return;
        }
        cert.put("sign", sign);

        // 保存位置
        String save = (String) options.get("save").value();
        // 保存私钥
        FileUtils.writeFile(String.format("%s%s-private.key", save, name), result.getPrivateKey());
        // 保存公钥
        FileUtils.writeFile(String.format("%s%s-public.key", save, name), result.getPublicKey());
        // 保存证书
        FileUtils.writeFile(String.format("%s%s-cert.json", save, name), JsonUtils.toJson(cert));
    }
}
