package top.xmln.cli.hash;

import top.xmln.option.Option;
import top.xmln.option.OptionsParser;
import top.xmln.option.OptionsRun;
import top.xmln.option.item.OptionString;
import top.xmln.utils.EncryptUtils;
import top.xmln.utils.FileUtils;
import top.xmln.utils.PrintUtils;

import java.util.Arrays;
import java.util.Map;

public class Hash implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 被哈希文件路径
        optionsParser.add(new OptionString(
                "file", Arrays.asList("-f", "--file"),
                null, null, "被哈希文件路径"
        ));
        // 哈希算法
        optionsParser.add(new OptionString(
                "algorithm", Arrays.asList("-a", "--algorithm"),
                "SHA-256", Arrays.asList("MD5", "SHA-1", "SHA-256", "SHA-512"), "哈希算法"
        ));
        // 哈希结果输出路径
        optionsParser.add(new OptionString(
                "outputPath", Arrays.asList("-op", "--output-path"),
                "./output/hash.txt", null, "哈希结果输出路径"
        ));
    }

    @Override
    public void run(Map<String, Option> options) {
        // 读取被哈希文件内容
        String file = (String) options.get("file").value();
        PrintUtils.infoFormat("读取被哈希文件内容: %s", file);
        String content = FileUtils.readFile(file);
        if (content == null) {
            PrintUtils.error("文件读取失败，无法计算哈希");
            return;
        }

        // 开始计算哈希
        String algorithm = (String) options.get("algorithm").value();
        PrintUtils.info("开始计算哈希...");
        String hash = EncryptUtils.digest(algorithm, content);
        if (hash == null) {
            PrintUtils.error("哈希计算失败");
            return;
        }
        PrintUtils.infoFormat("哈希结果：%s", hash);

        // 写入哈希结果文件
        String outputPath = (String) options.get("outputPath").value();
        PrintUtils.infoFormat("准备把哈希结果写入文件，写入位置：%s", outputPath);
        FileUtils.writeFile(outputPath, hash);
    }
}