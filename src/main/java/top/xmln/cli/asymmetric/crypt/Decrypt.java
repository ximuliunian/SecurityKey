package top.xmln.cli.asymmetric.crypt;

import top.xmln.option.Option;
import top.xmln.option.OptionsParser;
import top.xmln.option.OptionsRun;
import top.xmln.option.item.OptionBoolean;
import top.xmln.option.item.OptionString;
import top.xmln.utils.EncryptUtils;
import top.xmln.utils.FileUtils;
import top.xmln.utils.PrintUtils;

import java.util.Arrays;
import java.util.Map;

public class Decrypt implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 密钥文件路径
        optionsParser.add(new OptionString(
                "keyPath", Arrays.asList("-k", "--key-path"),
                null, null, "密钥文件路径（公钥/私钥）"
        ));
        // 待解密数据
        optionsParser.add(new OptionString(
                "data", Arrays.asList("-d", "--data"),
                null, null, "待解密数据"
        ));
        // 解密算法
        optionsParser.add(new OptionString(
                "algorithm", Arrays.asList("-a", "--algorithm"),
                "RSA", Arrays.asList("RSA", "ECC"), "密钥解密算法"
        ));
        // 是否写入文件
        optionsParser.add(new OptionBoolean(
                "writeToFile", Arrays.asList("-w", "--write"),
                false, "是否将结果写入文件"
        ));
        // 输出文件路径
        optionsParser.add(new OptionString(
                "outputPath", Arrays.asList("-o", "--output"),
                "./output/decrypted.txt", null, "解密结果输出路径"
        ));
    }

    @Override
    public void run(Map<String, Option> options) {
        // 读取密钥文件
        String keyPath = (String) options.get("keyPath").value();
        PrintUtils.infoFormat("读取密钥文件: %s", keyPath);
        String key = FileUtils.readFile(keyPath);
        if (key == null) {
            PrintUtils.error("密钥文件读取失败");
            return;
        }

        // 获取待解密数据
        String data = (String) options.get("data").value();
        PrintUtils.infoFormat("待解密数据: %s", data);

        // 获取解密算法
        String algorithm = (String) options.get("algorithm").value();
        PrintUtils.infoFormat("使用算法: %s", algorithm);

        // 执行解密
        PrintUtils.info("开始解密...");
        String decrypted = EncryptUtils.asymmetricDecrypt(algorithm, key, data);
        if (decrypted == null) {
            PrintUtils.error("解密失败");
            return;
        }

        // 打印解密结果
        PrintUtils.infoFormat("解密结果: %s", decrypted);

        // 检查是否需要写入文件
        boolean writeToFile = (boolean) options.get("writeToFile").value();
        if (writeToFile) {
            String outputPath = (String) options.get("outputPath").value();
            PrintUtils.infoFormat("准备写入文件: %s", outputPath);
            boolean success = FileUtils.writeFile(outputPath, decrypted);
            if (success) {
                PrintUtils.success("解密结果已写入文件");
            } else {
                PrintUtils.error("写入文件失败");
            }
        }
    }
}