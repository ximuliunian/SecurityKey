package top.xmln.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件工具类
 */
public class FileUtils {
    /**
     * 写入文件
     *
     * @param filePath 文件路径
     * @param content  内容
     * @return true:写入成功 false:写入失败
     */
    public static boolean writeFile(String filePath, String content) {
        File privateKeyFile = new File(filePath);
        if (!privateKeyFile.getParentFile().exists()) {
            PrintUtils.warningFormat("父目录不存在，自动创建：%s", privateKeyFile.getParentFile().getPath());
            privateKeyFile.getParentFile().mkdirs();
        }

        try (FileWriter privateKeyWriter = new FileWriter(privateKeyFile)) {
            PrintUtils.infoFormat("开始写入文件：%s", filePath);
            privateKeyWriter.write(content);
            return true;
        } catch (IOException e) {
            PrintUtils.errorFormat("写入文件失败：%s，路径：%s", e.getMessage(), filePath);
            return false;
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static String readFile(String filePath) {
        PrintUtils.infoFormat("开始读取文件：%s", filePath);
        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException e) {
            PrintUtils.errorFormat("读取文件失败：%s，路径：%s", e.getMessage(), filePath);
            return null;
        }
    }
}
