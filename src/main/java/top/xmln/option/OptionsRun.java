package top.xmln.option;

import java.util.Map;

@FunctionalInterface
public interface OptionsRun {
    /**
     * 执行选项
     *
     * @param options 选项
     */
    void run(Map<String, Option> options);
}
