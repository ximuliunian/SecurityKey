package top.xmln;

import top.xmln.cli.RootOption;
import top.xmln.cli.gen.GenerateCert;
import top.xmln.cli.gen.GenerateKey;
import top.xmln.cli.sign.Sign;
import top.xmln.option.OptionsParser;

public class Main {
    public static void main(String[] args) {
        // 根命令
        OptionsParser rootOptionsParser = OptionsParser.createRoot(new RootOption(), "格式：子命令/选项标志 ─ 选项类型(默认值)[可选值] ─ 帮助信息");

        // 生成密钥
        OptionsParser genKey = OptionsParser.createChild(rootOptionsParser, "gen-key", "生成密钥对", new GenerateKey());

        // 带证书的密钥对
        OptionsParser.createChild(genKey, "cert", "生成带证书的密钥对", new GenerateCert());

        // 对文件内容进行签名
        OptionsParser.createChild(rootOptionsParser, "sign", "对文件内容进行签名", new Sign());

        rootOptionsParser.parse(args);
    }
}