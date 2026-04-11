package top.xmln.cli.gen;

import top.xmln.option.Option;
import top.xmln.option.OptionsParser;
import top.xmln.option.OptionsRun;
import top.xmln.option.item.OptionString;
import top.xmln.utils.EncryptUtils;
import top.xmln.utils.FileUtils;
import top.xmln.utils.PrintUtils;

import java.util.Arrays;
import java.util.Map;

public class GenerateKey implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 密钥对生成算法
        optionsParser.add(new OptionString(
                "algorithm", Arrays.asList("-a", "--algorithm"),
                "RSA", Arrays.asList("RSA", "ECC"), "密钥对生成算法"
        ));
        // 保存位置
        optionsParser.add(new OptionString(
                "save", Arrays.asList("-s", "--save"),
                "./output/", null, "保存密钥对的目录（带/）"
        ));
    }

    @Override
    public void run(Map<String, Option> options) {
        String algorithm = (String) options.get("algorithm").value();
        String save = (String) options.get("save").value();

        // 生成密钥对
        EncryptUtils.AsymmetricResult result = EncryptUtils.asymmetric(algorithm);
        if (result == null) {
            return;
        }

        // 保存密钥对
        PrintUtils.infoFormat("密钥对保存位置：%s", save);
        FileUtils.writeFile(save + "public.key", result.getPublicKey());
        FileUtils.writeFile(save + "private.key", result.getPrivateKey());
        PrintUtils.success("密钥对生成成功");
    }
}
