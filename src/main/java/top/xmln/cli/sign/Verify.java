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

public class Verify implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 签名文件路径
        optionsParser.add(new OptionString(
                "signFile", Arrays.asList("-sf", "--sign-file"),
                null, null, "签名文件路径"
        ));
        // 原始数据文件路径
        optionsParser.add(new OptionString(
                "originalFile", Arrays.asList("-of", "--original-file"),
                null, null, "原始数据文件路径"
        ));
        // 签名密钥公钥数据路径
        optionsParser.add(new OptionString(
                "publicPath", Arrays.asList("-p", "--public-path"),
                "./output/public.key", null, "签名密钥公钥数据路径"
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
    }

    @Override
    public void run(Map<String, Option> options) {
        // 读取签名文件内容
        String file = (String) options.get("signFile").value();
        PrintUtils.infoFormat("读取签名文件内容: %s", file);
        String signContent = FileUtils.readFile(file);

        // 读取原始数据文件内容
        String originalFile = (String) options.get("originalFile").value();
        PrintUtils.infoFormat("读取原始数据文件内容: %s", originalFile);
        String originalContent = FileUtils.readFile(originalFile);

        // 读取签名密钥公钥数据
        String publicPath = (String) options.get("publicPath").value();
        PrintUtils.infoFormat("读取签名密钥公钥数据: %s", publicPath);
        String publicKey = FileUtils.readFile(publicPath);

        // 签名密钥算法
        String signAlgorithm = (String) options.get("signAlgorithm").value();
        String digestAlgorithm = (String) options.get("digestAlgorithm").value();
        String standardSignAlgorithm = EncryptUtils.signAsymmetric(signAlgorithm, digestAlgorithm);
        PrintUtils.info("开始准备验证签名...");

        // 验证签名
        Boolean verify = EncryptUtils.verify(standardSignAlgorithm, publicKey, originalContent, signContent);
        if (verify == null) {
            return;
        }
        PrintUtils.successFormat("签名结果：%s", verify ? "正确" : "错误");
    }
}
