package top.xmln.cli.digest;

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

public class Digest implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 被计算摘要文件路径
        optionsParser.add(new OptionString(
                "file", Arrays.asList("-f", "--file"),
                null, null, "被计算摘要文件路径"
        ));
        // 摘要算法
        optionsParser.add(new OptionString(
                "algorithm", Arrays.asList("-a", "--algorithm"),
                "SHA-256", Arrays.asList("MD5", "SHA-1", "SHA-256", "SHA-512"), "摘要算法"
        ));
        // 是否输出到文件
        optionsParser.add(new OptionBoolean(
                "outputToFile", Arrays.asList("-of", "--output-to-file"),
                false, "是否输出到文件"
        ));
        // 摘要结果输出路径
        optionsParser.add(new OptionString(
                "outputPath", Arrays.asList("-op", "--output-path"),
                "./output/digest.txt", null, "摘要结果输出路径（只有确认输出到文件时才生效）"
        ));
    }

    @Override
    public void run(Map<String, Option> options) {
        // 读取被计算摘要文件内容
        String file = (String) options.get("file").value();
        PrintUtils.infoFormat("读取被计算摘要文件内容: %s", file);
        String content = FileUtils.readFile(file);
        if (content == null) {
            PrintUtils.error("文件读取失败，无法计算摘要");
            return;
        }

        // 开始计算摘要
        String algorithm = (String) options.get("algorithm").value();
        PrintUtils.info("开始计算摘要...");
        String hash = EncryptUtils.digest(algorithm, content);
        if (hash == null) {
            PrintUtils.error("摘要计算失败");
            return;
        }
        PrintUtils.infoFormat("摘要结果：%s", hash);

        if ((Boolean) options.get("outputToFile").value()) {
            // 写入摘要结果文件
            String outputPath = (String) options.get("outputPath").value();
            PrintUtils.infoFormat("准备把摘要结果写入文件，写入位置：%s", outputPath);
            FileUtils.writeFile(outputPath, hash);
        }
    }
}