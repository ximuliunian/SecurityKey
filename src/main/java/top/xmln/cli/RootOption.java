package top.xmln.cli;

import top.xmln.option.Option;
import top.xmln.option.OptionsParser;
import top.xmln.option.OptionsRun;
import top.xmln.option.item.OptionFunction;

import java.util.Arrays;
import java.util.Map;

public class RootOption implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        optionsParser.add(new OptionFunction("info", Arrays.asList("-i", "--info"), "（つ﹏⊂）"));
    }

    @Override
    public void run(Map<String, Option> options) {
        if (options.containsKey("info")) {
            System.out.println("https://www.ximuliunian.top");
        }
    }
}
