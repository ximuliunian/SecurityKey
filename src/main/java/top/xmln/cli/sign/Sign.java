package top.xmln.cli.sign;

import top.xmln.option.Option;
import top.xmln.option.OptionsParser;
import top.xmln.option.OptionsRun;
import top.xmln.option.item.OptionString;
import top.xmln.utils.EncryptUtils;
import top.xmln.utils.FileUtils;
import top.xmln.utils.PrintUtils;

import java.util.Arrays;
import java.util.Map;

public class Sign implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 被签名文件路径
        optionsParser.add(new OptionString(
                "file", Arrays.asList("-f", "--file"),
                null, null, "被签名文件路径"
        ));
        // 签名密钥私钥数据路径
        optionsParser.add(new OptionString(
                "privatePath", Arrays.asList("-p", "--private-path"),
                "./output/private.key", null, "签名密钥私钥数据路径"
        ));
        // 签名密钥算法
        optionsParser.add(new OptionString(
                "signAlgorithm", Arrays.asList("-sa", "--sign-algorithm"),
                "RSA", Arrays.asList("RSA", "ECC"), "签名密钥算法"
        ));
        // 摘要算法
        optionsParser.add(new OptionString(
                "digestAlgorithm", Arrays.asList("-da", "--digest-algorithm"),
                "SHA-256", Arrays.asList("SHA-256", "SHA-512"), "摘要算法"
        ));
        // 签名文件输出路径
        optionsParser.add(new OptionString(
                "signOutputPath", Arrays.asList("-sop", "--sign-output-path"),
                "./output/sign.txt", null, "签名文件输出路径"
        ));
    }

    @Override
    public void run(Map<String, Option> options) {
        // 读取被签名文件内容
        String file = (String) options.get("file").value();
        PrintUtils.infoFormat("读取被签名文件内容: %s", file);
        String content = FileUtils.readFile(file);

        // 读取签名密钥私钥数据
        String signPath = (String) options.get("privatePath").value();
        PrintUtils.infoFormat("读取签名密钥私钥数据: %s", signPath);
        String privateKey = FileUtils.readFile(signPath);

        // 开始签名
        String signAlgorithm = (String) options.get("signAlgorithm").value();
        String digestAlgorithm = (String) options.get("digestAlgorithm").value();
        String standardSignAlgorithm = EncryptUtils.signAsymmetric(signAlgorithm, digestAlgorithm);
        PrintUtils.info("开始准备签名...");
        String sign = EncryptUtils.sign(standardSignAlgorithm, privateKey, content);
        PrintUtils.infoFormat("签名：%s", sign);

        // 写入签名文件
        String signOutputPath = (String) options.get("signOutputPath").value();
        PrintUtils.infoFormat("准备把签名写入文件，写入位置：%s", signOutputPath);
        FileUtils.writeFile(signOutputPath, sign);
    }
}
