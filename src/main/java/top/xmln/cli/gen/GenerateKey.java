package top.xmln.cli.gen;

import top.xmln.option.*;
import top.xmln.utils.EncryptUtils;
import top.xmln.utils.FileUtils;
import top.xmln.utils.PrintUtils;

import java.util.Arrays;
import java.util.Map;

public class GenerateKey implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 加密算法
        optionsParser.add(new OptionsItem("algorithm", Arrays.asList("-a", "--algorithm"), Type.String, "RSA", "加密算法（RSA、ECC）"));
        // 保存位置
        optionsParser.add(new OptionsItem("save", Arrays.asList("-s", "--save"), Type.String, "./output/", "保存密钥对的目录（带/）"));
    }

    @Override
    public void run(Map<String, Option> options) {
        String algorithm = (String) options.get("algorithm").value();
        PrintUtils.info("加密算法：" + algorithm);

        String save = (String) options.get("save").value();
        PrintUtils.info("保存位置：" + save);

        // 生成密钥对
        EncryptUtils.AsymmetricResult result = EncryptUtils.asymmetric(algorithm);
        if (result == null) {
            return;
        }

        // 保存密钥对
        FileUtils.writeFile(save + "public.key", result.getPublicKey());
        FileUtils.writeFile(save + "private.key", result.getPrivateKey());
    }
}
