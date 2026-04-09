package top.xmln.cli.gen;

import top.xmln.option.*;

import java.util.Arrays;
import java.util.Map;

public class GenerateCert implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        // 过期时间（毫秒时间戳）
        optionsParser.add(new OptionsItem("expire", Arrays.asList("-e", "--expire"), Type.Number, null, "证书过期时间（毫秒时间戳）"));
        // 加密算法
        optionsParser.add(new OptionsItem("algorithm", Arrays.asList("-a", "--algorithm"), Type.String, "RSA", "加密算法（RSA、ECC）"));
        // 保存位置
        optionsParser.add(new OptionsItem("save", Arrays.asList("-s", "--save"), Type.String, "./output/", "保存带证书的密钥对目录（带/）"));
        // 证书名称
        optionsParser.add(new OptionsItem("name", Arrays.asList("-n", "--name"), Type.String, "xmln", "证书名称"));
        // 签发私钥路径
        optionsParser.add(new OptionsItem("signPath", Arrays.asList("-sp", "--signPath"), Type.String, "./output/private.key", "签发私钥路径"));
    }

    @Override
    public void run(Map<String, Option> options) {

    }
}
