package top.xmln.option.item;

import top.xmln.option.Type;
import top.xmln.utils.PrintUtils;

import java.math.BigDecimal;
import java.util.List;

public class OptionNumber extends AbstractOptions {
    /**
     * 选项类型
     */
    private final List<BigDecimal> choices;

    /**
     * @param name         选项名称
     * @param args         选项
     * @param defaultValue 默认参数（没有的话那么该参数为必填项）
     * @param help         描述信息
     */
    public OptionNumber(String name, List<String> args, BigDecimal defaultValue, List<BigDecimal> choices, String help) {
        super(name, args, Type.Number, defaultValue, help);
        this.choices = choices;
    }

    @Override
    public boolean verify(String value) {
        if (choices == null) {
            return true;
        }
        BigDecimal val = new BigDecimal(value);
        for (BigDecimal choice : choices) {
            if (val.compareTo(choice) == 0) {
                return true;
            }
        }
        List<String> list = choices.stream().map(BigDecimal::toString).toList();
        PrintUtils.errorFormat("%s 选项值 %s 不在可选值 %s 中", args, value, String.join(", ", list));
        return false;
    }

    @Override
    protected void getOptionTypeStr(StringBuilder sb) {
        super.getOptionTypeStr(sb);
        if (choices != null && !choices.isEmpty()) {
            sb.append(choices.stream().map(BigDecimal::toString).toList());
        }
    }
}
