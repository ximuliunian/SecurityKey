package top.xmln.option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令行选项解析器
 */
public final class OptionsParser {
    /**
     * 名称
     */
    private final String name;
    /**
     * 描述
     */
    private final String help;
    /**
     * 执行命令
     */
    private final OptionsRun runFun;
    /**
     * 子选项解析器
     */
    private final Map<String, OptionsParser> childOptions = new HashMap<>();
    /**
     * 选项信息
     */
    private final Map<String, OptionsItem> optionsMap = new HashMap<>();

    /**
     * 子命令构造
     *
     * @param name   名称
     * @param help   帮助信息
     * @param runFun 执行命令
     */

    private OptionsParser(String name, String help, OptionsRun runFun) {
        this.name = name;
        this.help = help;
        this.runFun = runFun;
    }

    /**
     * 根选项构造工厂
     *
     * @param runFun 执行命令
     * @param help   帮助信息
     * @return 根选项解析器
     */
    public static OptionsParser createRoot(OptionsRun runFun, String help) {
        OptionsParser rootParser = new OptionsParser("root", help, runFun);
        runFun.register(rootParser);
        return rootParser;
    }

    /**
     * 子命令构造工厂
     *
     * @param parent 父选项解析器
     * @param name   子命令名称
     * @param help   子命令描述信息
     * @param runFun 执行命令
     * @return 子命令解析器
     */
    public static OptionsParser createChild(OptionsParser parent, String name, String help, OptionsRun runFun) {
        OptionsParser childParser = new OptionsParser(name, help, runFun);
        parent.childOptions.put(name, childParser);
        runFun.register(childParser);
        return childParser;
    }

    /**
     * 添加选项
     *
     * @param optionsItem 选项
     * @return 本类
     */
    public OptionsParser add(OptionsItem optionsItem) {
        optionsMap.put(optionsItem.name(), optionsItem);
        return this;
    }

    /**
     * 选项解析
     *
     * @param args 选项参数
     */
    public void parse(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println(this);
            return;
        }

        int i = 0;

        // 是否以-开头
        String arg = args[i];
        if (arg.startsWith("-")) {
            // 解析并执行命令
            Map<String, Option> options = parseString(0, args, optionsMap);
            if (options == null) return;
            this.runFun.run(options);
            return;
        }

        // 解析子命令
        OptionsParser parser = childOptions.get(arg);
        while (true) {
            if (parser == null) {
                System.out.println("问题：没有该子命令[" + arg + "]，可用子命令：" + childOptions.keySet());
                break;
            }

            // 添加解析 孙、曾孙、... 命令的能力
            if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                i++;
                parser = parser.childOptions.get(args[i]);
                continue;
            }

            // 解析选项并执行命令
            Map<String, Option> options = parseString(i + 1, args, parser.optionsMap);
            if (options == null) return;
            parser.runFun.run(options);
            break;
        }
    }

    /**
     * 解析选项
     *
     * @param startIndex 开始索引
     * @param args       选项参数
     * @param options    选项信息
     * @return 解析后的选项
     */
    private Map<String, Option> parseString(int startIndex, String[] args, Map<String, OptionsItem> options) {
        Map<String, Option> result = new HashMap<>();
        // 先把所有的默认选项添加到结果中
        for (OptionsItem value : options.values()) {
            if (value.defaultValue() != null) {
                result.put(value.name(), new Option(value.name(), value.defaultValue(), value.type()));
            }
        }

        // 开始解析
        for (int i = startIndex; i < args.length; i++) {
            int splitIndex = args[i].indexOf("=");
            // 判断是否为纯选项的场景
            if (splitIndex == -1) {
                splitIndex = args[i].length();
            }
            String key = args[i].substring(0, splitIndex);
            String val;
            // 如果是纯选项，那么值为null，否则值为 = 后面的内容
            if (key.length() == args[i].length()) {
                val = null;
            } else {
                val = args[i].substring(splitIndex + 1);
            }

            for (OptionsItem value : options.values()) {
                if (!value.args().contains(key)) continue;

                switch (value.type()) {
                    // 布尔类型
                    case Boolean -> {
                        /*
                         * 对于布尔类型如果没有值而且默认值不为空的情况下
                         * 如果默认值为true，那么就添加false，否则就添加true
                         * */
                        if (val == null || val.isBlank()) {
                            if (value.defaultValue() != null) {
                                result.put(value.name(), new Option(value.name(), !((boolean) value.defaultValue()), value.type()));
                            }
                            continue;
                        }

                        // 有值则添加到结果中
                        if ("true".equals(val) || "false".equals(val)) {
                            result.put(value.name(), new Option(value.name(), Boolean.parseBoolean(val), value.type()));
                        } else {
                            System.out.println("问题：参数[" + value.name() + "]必须是true/false，当前值：" + val);
                            return null;
                        }
                    }

                    // 字符串类型
                    case String -> {
                        if (val == null || val.isBlank()) continue;
                        result.put(value.name(), new Option(value.name(), val, value.type()));
                    }

                    // 整数类型
                    case Integer -> {
                        if (val == null || val.isBlank()) continue;
                        // 整数类型校验
                        try {
                            result.put(value.name(), new Option(value.name(), Integer.parseInt(val), value.type()));
                        } catch (NumberFormatException e) {
                            System.out.println("问题：参数[" + value.name() + "]必须是整数，当前值：" + val);
                            return null;
                        }
                    }

                    // 方法类型
                    case Function -> {
                        result.put(value.name(), new Option(value.name(), null, value.type()));
                    }
                }
            }
        }

        // 检查一下必选参数是否为空
        if (options.size() != result.size()) {
            System.out.println("问题：必填参数为空");
            return null;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(help).append("\n");

        int totalItems = optionsMap.size() + childOptions.size();
        int currentIndex = 0;

        // 打印根选项
        for (OptionsItem item : optionsMap.values()) {
            currentIndex++;
            boolean isLast = currentIndex == totalItems;
            sb.append(isLast ? "└─ " : "├─ ").append(item).append("\n");
        }

        // 统一打印子命令
        printChildren(sb, childOptions, "");
        return sb.toString();
    }

    /**
     * 递归打印子命令
     *
     * @param sb       缓存字符串
     * @param children 子命令
     * @param prefix   前缀
     */
    private void printChildren(StringBuilder sb, Map<String, OptionsParser> children, String prefix) {
        if (children.isEmpty()) return;

        int index = 0;
        int size = children.size();
        for (OptionsParser child : children.values()) {
            index++;
            boolean isLast = index == size;

            // 打印子命令名称
            sb.append(prefix).append(isLast ? "└─ " : "├─ ")
                    .append(child.name).append("：").append(child.help).append("\n");

            // 打印选项
            String indent = isLast ? "   " : "│  ";
            var options = new ArrayList<>(child.optionsMap.values());
            for (int i = 0; i < options.size(); i++) {
                boolean lastOpt = i == options.size() - 1 && child.childOptions.isEmpty();
                sb.append(prefix).append(indent).append(lastOpt ? "└─ " : "├─ ").append(options.get(i)).append("\n");
            }
            // 递归嵌套子命令
            printChildren(sb, child.childOptions, prefix + (isLast ? "   " : "│  "));
        }
    }
}