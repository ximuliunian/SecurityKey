package top.xmln.option.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.xmln.option.Type;

import java.util.List;

/**
 * 命令行参数
 */
@AllArgsConstructor
@Getter
public abstract class AbstractOptions<T> {
    /**
     * 选项名称
     */
    protected String name;
    /**
     * 选项
     */
    protected List<String> args;
    /**
     * 选项类型
     */
    protected Type type;
    /**
     * 默认参数（没有的话那么该参数为必填项）
     */
    protected Object defaultValue;
    /**
     * 描述信息
     */
    protected String help;

    /**
     * 验证选项是否符合要求
     *
     * @return true:符合要求 false:不符合要求
     */
    public boolean verify(T value) {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // 选项标志
        sb.append(args.toString()).append("  ");

        // 选项类型
        if (type == Type.Function) {
            // 功能选项
            sb.append(type.getName());
        } else if (defaultValue != null) {
            // 有默认值的选项
            sb.append(String.format("%s（%s）", type.getName(), defaultValue));
        } else {
            // 必填项选项
            sb.append(type.getName()).append("（必填）");
        }
        sb.append("  ");

        // 帮助信息
        sb.append(help);
        return sb.toString();
    }
}
