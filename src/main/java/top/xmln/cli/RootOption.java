package top.xmln.cli;

import top.xmln.option.*;

import java.util.Arrays;
import java.util.Map;

public class RootOption implements OptionsRun {
    @Override
    public void register(OptionsParser optionsParser) {
        optionsParser.add(new OptionsItem("version", Arrays.asList("-v", "--version"), Type.Function, null, "显示版本"));
    }

    @Override
    public void run(Map<String, Option> options) {

    }
}
