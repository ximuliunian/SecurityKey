package top.xmln;

import top.xmln.option.OptionsItem;
import top.xmln.option.OptionsParser;
import top.xmln.option.Type;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // 根命令
        OptionsParser rootOptionsParser = new OptionsParser(System.out::println);
        rootOptionsParser.add(new OptionsItem("version", Arrays.asList("-v", "--version"), Type.Function, null, "显示版本"));

        // 初始化配置文件
        OptionsParser initConfig = new OptionsParser(rootOptionsParser, "init-config", "初始化配置文件", System.out::println);

        // 生成密钥
        OptionsParser generateKey = new OptionsParser(rootOptionsParser, "gen-key", "生成密钥", System.out::println);

        // 解析并执行选项
        rootOptionsParser.parse(args);
    }
}