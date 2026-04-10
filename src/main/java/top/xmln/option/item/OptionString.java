package top.xmln.option.item;

import top.xmln.option.Type;
import top.xmln.utils.PrintUtils;

import java.util.List;

public class OptionString extends AbstractOptions<String> {
    /**
     * 可选值
     */
    private final List<String> choices;

    /**
     * @param name         选项名称
     * @param args         选项
     * @param defaultValue 默认参数（没有的话那么该参数为必填项）
     * @param choices      可选值
     * @param help         描述信息
     */
    public OptionString(String name, List<String> args, String defaultValue, List<String> choices, String help) {
        super(name, args, Type.String, defaultValue, help);
        this.choices = choices;
    }

    @Override
    public boolean verify(String value) {
        if (choices == null) {
            return true;
        }

        for (String choice : choices) {
            if (value.equals(choice)) {
                return true;
            }
        }
        PrintUtils.error(String.format("%s 选项值 %s 不在可选值 %s 中", name, value, choices));
        return false;
    }

    @Override
    protected void getOptionTypeStr(StringBuilder sb) {
        super.getOptionTypeStr(sb);
        if (choices != null && !choices.isEmpty()) {
            sb.append(choices);
        }
    }
}
