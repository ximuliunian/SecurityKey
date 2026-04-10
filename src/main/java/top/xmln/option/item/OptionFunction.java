package top.xmln.option.item;

import top.xmln.option.Type;

import java.util.List;

public class OptionFunction extends AbstractOptions {
    /**
     * @param name 选项名称
     * @param args 选项
     * @param help 描述信息
     */
    public OptionFunction(String name, List<String> args, String help) {
        super(name, args, Type.Function, null, help);
    }
}
