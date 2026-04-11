package top.xmln.utils;

/**
 * 美化版控制台打印工具类
 * 支持：彩色输出、加粗、图标标识，提升日志可读性
 */
public class PrintUtils {
    /**
     * 重置所有样式（必须加，否则颜色会污染后续打印）
     */
    private static final String RESET = "\033[0m";
    /**
     * 文字加粗
     */
    private static final String BOLD = "\033[1m";
    /**
     * 前景色：绿色（成功）
     */
    private static final String GREEN = "\033[32m";
    /**
     * 前景色：红色（错误）
     */
    private static final String RED = "\033[31m";
    /**
     * 前景色：蓝色（信息）
     */
    private static final String BLUE = "\033[34m";
    /**
     * 前景色：黄色（警告）
     */
    private static final String YELLOW = "\033[33m";


    /**
     * 打印成功信息
     *
     * @param message 要打印的成功信息
     */
    public static void success(String message) {
        System.out.println(BOLD + GREEN + "成功 " + message + RESET);
    }

    /**
     * 打印失败信息
     *
     * @param message 要打印的失败信息
     */
    public static void error(String message) {
        System.out.println(BOLD + RED + "失败 " + message + RESET);
    }

    /**
     * 打印普通信息
     *
     * @param message 要打印的信息
     */
    public static void info(String message) {
        System.out.println(BOLD + BLUE + "信息 " + message + RESET);
    }

    /**
     * 打印警告信息
     *
     * @param message 要打印的警告信息
     */
    public static void warning(String message) {
        System.out.println(BOLD + YELLOW + "警告 " + message + RESET);
    }

    /**
     * 打印成功信息
     *
     * @param message 要打印的成功信息
     */
    public static void successFormat(String message, Object... params) {
        success(String.format(message, params));
    }

    /**
     * 打印失败信息
     *
     * @param message 要打印的失败信息
     */
    public static void errorFormat(String message, Object... params) {
        error(String.format(message, params));
    }

    /**
     * 打印普通信息
     *
     * @param message 要打印的信息
     */
    public static void infoFormat(String message, Object... params) {
        info(String.format(message, params));
    }

    /**
     * 打印警告信息
     *
     * @param message 要打印的警告信息
     */
    public static void warningFormat(String message, Object... params) {
        warning(String.format(message, params));
    }
}