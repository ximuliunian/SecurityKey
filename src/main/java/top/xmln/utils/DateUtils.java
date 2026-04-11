package top.xmln.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化工具类
     */
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /**
     * 毫秒时间戳转换为日期字符串
     *
     * @param timestamp 时间戳
     * @return 日期字符串
     */
    public static String millisecondTimestampToDateString(long timestamp) {
        return DATE_FORMATTER.format(new Date(timestamp));
    }

    /**
     * 秒时间戳转换为日期字符串
     *
     * @param timestamp 时间戳
     * @return 日期字符串
     */
    public static String secondTimestampToDateString(long timestamp) {
        return DATE_FORMATTER.format(new Date(timestamp * 1000));
    }
}
