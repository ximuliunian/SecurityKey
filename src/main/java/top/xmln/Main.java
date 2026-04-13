package top.xmln;

import top.xmln.cli.RootOption;
import top.xmln.cli.asymmetric.GenerateCert;
import top.xmln.cli.asymmetric.GenerateKey;
import top.xmln.cli.asymmetric.crypt.Decrypt;
import top.xmln.cli.asymmetric.crypt.Encrypt;
import top.xmln.cli.digest.Digest;
import top.xmln.cli.sign.Sign;
import top.xmln.cli.sign.Verify;
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

        // 对签名进行验证
        OptionsParser.createChild(rootOptionsParser, "verify", "对签名进行验证", new Verify());

        // 加密数据
        OptionsParser.createChild(rootOptionsParser, "encrypt", "对数据进行非对称加密", new Encrypt());

        // 解密数据
        OptionsParser.createChild(rootOptionsParser, "decrypt", "对数据进行非对称解密", new Decrypt());

        // 计算文件摘要
        OptionsParser.createChild(rootOptionsParser, "digest", "计算文件摘要", new Digest());

        rootOptionsParser.parse(args);
    }
}