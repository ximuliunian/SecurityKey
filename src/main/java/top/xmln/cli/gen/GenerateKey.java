package top.xmln.cli.gen;

import top.xmln.option.*;
import top.xmln.utils.PrintUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
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

        try {
            PrintUtils.info("开始生成密钥对...");
            // 创建密钥对
            KeyPair keyPair = KeyPairGenerator.getInstance(algorithm).generateKeyPair();
            byte[] publicKey = keyPair.getPublic().getEncoded(); // 公钥
            byte[] privateKey = keyPair.getPrivate().getEncoded(); // 私钥

            // 保存密钥对
            File publicKeyFile = new File(save + "public.key");
            File privateKeyFile = new File(save + "private.key");
            publicKeyFile.getParentFile().mkdirs();
            privateKeyFile.getParentFile().mkdirs();

            // 写入文件
            try (
                    FileWriter publicKeyWriter = new FileWriter(publicKeyFile);
                    FileWriter privateKeyWriter = new FileWriter(privateKeyFile);
            ) {
                publicKeyWriter.write(Base64.getEncoder().encodeToString(publicKey));
                privateKeyWriter.write(Base64.getEncoder().encodeToString(privateKey));
                PrintUtils.success("密钥对生成成功");
            } catch (IOException e) {
                PrintUtils.error("写入文件密钥对失败");
            }
        } catch (NoSuchAlgorithmException e) {
            PrintUtils.error("加密算法不存在");
        }
    }
}
