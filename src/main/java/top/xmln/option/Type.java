package top.xmln.option;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 命令行参数类型
 */
@AllArgsConstructor
@Getter
public enum Type {
    Integer("整数"), String("字符串"), Boolean("布尔值"), Function("功能");
    private final String name;
}
