package top.xmln.cli.gen;

import top.xmln.option.Option;
import top.xmln.option.OptionsParser;
import top.xmln.option.OptionsRun;
import top.xmln.option.item.OptionNumber;
import top.xmln.option.item.OptionString;
import top.xmln.utils.EncryptUtils;
import top.xmln.utils.FileUtils;
import top.xmln.utils.JsonUtils;
import top.xmln.utils.PrintUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenerateCert implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 过期时间（毫秒时间戳）
        optionsParser.add(new OptionNumber(
                "expire", Arrays.asList("-e", "--expire"),
                null, null, "证书过期时间（毫秒时间戳）"
        ));
        // 签名密钥私钥路径
        optionsParser.add(new OptionString(
                "signPath", Arrays.asList("-sp", "--sign-path"),
                "./output/private.key", null, "签名密钥私钥路径"
        ));
        // 签名密钥算法
        optionsParser.add(new OptionString(
                "signAlgorithm", Arrays.asList("-sa", "--sign-algorithm"),
                "RSA", Arrays.asList("RSA", "ECC"), "签名密钥算法"
        ));
        // 密钥对生成算法
        optionsParser.add(new OptionString(
                "algorithm", Arrays.asList("-a", "--algorithm"),
                "RSA", Arrays.asList("RSA", "ECC"), "密钥对生成算法"
        ));
        // 保存位置
        optionsParser.add(new OptionString(
                "save", Arrays.asList("-s", "--save"),
                "./output/", null, "保存带证书的密钥对目录（带/）"
        ));
        // 证书名称
        optionsParser.add(new OptionString(
                "name", Arrays.asList("-n", "--name"),
                "xmln", null, "证书名称"
        ));
        // 摘要算法
        optionsParser.add(new OptionString(
                "digestAlgorithm", Arrays.asList("-da", "--digest-algorithm"),
                "SHA-256", Arrays.asList("SHA-256", "SHA-512"), "摘要算法"
        ));
    }

    @Override
    public void run(Map<String, Option> options) {
        PrintUtils.info("开始生成证书");
        Map<String, String> cert = new HashMap<>();
        // 证书ID
        String id = UUID.randomUUID().toString();
        cert.put("id", id);
        PrintUtils.infoFormat("证书ID：%s", id);
        // 证书名称
        String name = (String) options.get("name").value();
        cert.put("name", name);
        PrintUtils.infoFormat("证书名称：%s", name);
        // 签发时间
        long signTime = System.currentTimeMillis();
        cert.put("signTime", String.valueOf(System.currentTimeMillis()));
        PrintUtils.infoFormat("签发时间：%s", signTime);
        // 加密算法
        String algorithm = (String) options.get("algorithm").value();
        cert.put("algorithm", algorithm);
        PrintUtils.infoFormat("密钥对生成算法：%s", algorithm);
        // 过期时间（毫秒时间戳）
        long expireTime = System.currentTimeMillis() + Long.parseLong(options.get("expire").value().toString());
        cert.put("expireTime", String.valueOf(expireTime));
        PrintUtils.infoFormat("过期时间：%s", expireTime);
        // 摘要算法
        String digestAlgorithm = (String) options.get("digestAlgorithm").value();
        cert.put("digestAlgorithm", digestAlgorithm);
        PrintUtils.infoFormat("摘要算法：%s", digestAlgorithm);
        // 签名密钥算法
        String signAlgorithm = (String) options.get("signAlgorithm").value();
        cert.put("signAlgorithm", signAlgorithm);
        PrintUtils.infoFormat("签名密钥算法：%s", signAlgorithm);

        // 生成密钥对
        EncryptUtils.AsymmetricResult result = EncryptUtils.asymmetric(algorithm);
        if (result == null) {
            return;
        }

        // 公钥
        cert.put("publicKey", result.getPublicKey());
        PrintUtils.infoFormat("证书公钥：%s", result.getPublicKey());

        // 私钥和证书内容
        String signPath = (String) options.get("signPath").value();
        PrintUtils.infoFormat("获取签名密钥私钥数据，路径：%s", signPath);
        String privateKey = FileUtils.readFile(signPath);
        String json = JsonUtils.toJson(cert);
        // 标准签名算法名
        String standardSignAlgorithm = EncryptUtils.signAsymmetric(signAlgorithm, digestAlgorithm);
        cert.put("signAlgorithm", standardSignAlgorithm);
        PrintUtils.infoFormat("签名算法：%s", standardSignAlgorithm);
        // 签名
        String sign = EncryptUtils.sign(standardSignAlgorithm, privateKey, json);
        if (sign == null) {
            return;
        }
        cert.put("sign", sign);
        PrintUtils.infoFormat("签名：%s", sign);

        // 保存位置
        String save = (String) options.get("save").value();
        PrintUtils.infoFormat("保存内容位置：%s", save);
        // 保存私钥
        FileUtils.writeFile(String.format("%s%s-private.key", save, name), result.getPrivateKey());
        // 保存公钥
        FileUtils.writeFile(String.format("%s%s-public.key", save, name), result.getPublicKey());
        // 保存证书
        FileUtils.writeFile(String.format("%s%s-cert.json", save, name), JsonUtils.toJson(cert));
        PrintUtils.success("证书生成成功");
    }
}
