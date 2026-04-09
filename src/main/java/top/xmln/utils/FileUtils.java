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
        privateKeyFile.getParentFile().mkdirs();

        // 写入文件
        try (FileWriter privateKeyWriter = new FileWriter(privateKeyFile)) {
            privateKeyWriter.write(content);
            PrintUtils.success(String.format("文件已保存到%s", filePath));
            return true;
        } catch (IOException e) {
            PrintUtils.error("写入文件失败");
            return false;
        }
    }

    /**
     * 读取文件内容
     */
    public static String readFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException e) {
            PrintUtils.error("读取文件失败");
            return null;
        }
    }
}
