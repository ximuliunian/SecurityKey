package top.xmln.option;

import java.util.List;

/**
 * 命令行参数
 *
 * @param name         选项名称
 * @param args         选项
 * @param type         选项类型
 * @param defaultValue 默认参数（没有的话那么该参数为必填项）
 * @param help         描述信息
 */
public record OptionsItem(
        String name,
        List<String> args,
        Type type,
        Object defaultValue,
        String help
) {
    @Override
    public String toString() {
        String defaultStr;
        if (type == Type.Function) {
            defaultStr = Type.Function.getName();
        } else if (defaultValue != null) {
            defaultStr = String.format("%s（%s）", type.getName(), defaultValue);
        } else {
            defaultStr = type.getName() + "（必填）";
        }

        return String.format("%s  %s  %s", args.toString(), defaultStr, help);
    }
}
