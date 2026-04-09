package top.xmln;

import top.xmln.cli.RootOption;
import top.xmln.cli.gen.GenerateCert;
import top.xmln.cli.gen.GenerateKey;
import top.xmln.option.OptionsParser;

public class Main {
    public static void main(String[] args) {
        // 根命令
        OptionsParser rootOptionsParser = OptionsParser.createRoot(new RootOption(), "格式：[子命令] [选项标志] 选项类型(默认值) 帮助信息");

        // 生成密钥
        OptionsParser genKey = OptionsParser.createChild(rootOptionsParser, "gen-key", "生成密钥对", new GenerateKey());

        // 带证书的密钥对
        OptionsParser.createChild(genKey, "cert", "生成带证书的密钥对", new GenerateCert());

        // 解析并执行选项
//        args = new String[]{"gen-key", "-a", "RSA", "-s", "./output/"};
        rootOptionsParser.parse(args);
    }
}