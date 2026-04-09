package top.xmln.option;

import java.util.Map;

/**
 * 选项执行器
 */
public interface OptionsRun {
    /**
     * 选项注册
     *
     * @param optionsParser 选项解析器
     */
    void register(OptionsParser optionsParser);

    /**
     * 执行选项
     *
     * @param options 选项参数 K：选项名称 V：选项值
     */
    void run(Map<String, Option> options);
}
