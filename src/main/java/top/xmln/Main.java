package top.xmln;

import top.xmln.cli.RootOption;
import top.xmln.option.Option;
import top.xmln.option.OptionsParser;
import top.xmln.option.OptionsRun;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 根命令
        OptionsParser rootOptionsParser = OptionsParser.createRoot(new RootOption(), "格式：[子命令] [选项标志] 选项类型(默认值) 帮助信息");

        // 初始化配置文件
        OptionsParser initConfig = OptionsParser.createChild(rootOptionsParser, "init-config", "初始化配置文件", new OptionsRun() {
            @Override
            public void register(OptionsParser optionsParser) {

            }

            @Override
            public void run(Map<String, Option> options) {

            }
        });

        // 生成密钥
        OptionsParser generateKey = OptionsParser.createChild(rootOptionsParser, "gen-key", "生成密钥", new OptionsRun() {
            @Override
            public void register(OptionsParser optionsParser) {

            }

            @Override
            public void run(Map<String, Option> options) {

            }
        });

        // 解析并执行选项
        rootOptionsParser.parse(args);
    }
}