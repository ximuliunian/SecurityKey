package top.xmln.option.item;

import top.xmln.option.Type;

import java.util.List;

public class OptionBoolean extends AbstractOptions<Boolean> {
    /**
     * @param name         选项名称
     * @param args         选项
     * @param defaultValue 默认参数（没有的话那么该参数为必填项）
     * @param help         描述信息
     */
    public OptionBoolean(String name, List<String> args, Boolean defaultValue, String help) {
        super(name, args, Type.Boolean, defaultValue, help);
    }
}
