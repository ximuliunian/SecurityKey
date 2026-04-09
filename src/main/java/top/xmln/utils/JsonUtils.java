package top.xmln.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

/**
 * JSON工具类
 */
public class JsonUtils extends ObjectMapper {
    private static JsonUtils objectMapper;

    public static JsonUtils getInstance() {
        if (objectMapper == null) {
            objectMapper = new JsonUtils();

            // 忽略未知属性
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 允许单引号
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            // 允许注释
            objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

        }
        return objectMapper;
    }

    /**
     * 将对象序列化为JSON字符串
     *
     * @param o 对象
     * @return JSON字符串
     */
    public static String toJson(Object o) {
        try {
            return getInstance().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 序列化失败");
        }
    }

    /**
     * 将JSON字符串反序列化为对象
     *
     * @param json JSON字符串
     * @return 对象
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return getInstance().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 反序列化失败");
        }
    }

    /**
     * 将Map转换为JSONNode
     *
     * @param map Map对象
     * @return JSONNode对象
     */
    public static JsonNode toJsonNode(Map<String, Object> map) {
        return getInstance().convertValue(map, JsonNode.class);
    }

    /**
     * 修改JSON对象
     *
     * @param node     JSON对象
     * @param consumer 修改函数
     */
    public static void editJson(JsonNode node, Consumer<ObjectNode> consumer) {
        consumer.accept((ObjectNode) node);
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @return 对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return getInstance().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 反序列化失败", e);
        }
    }

    /**
     * 从JSON文件中读取对象并处理
     *
     * @param file  JSON文件
     * @param clazz 对象类型
     * @return 处理结果
     */
    public static <T> T readJsonFile(File file, Class<T> clazz) {
        T archiveInfo;
        try {
            archiveInfo = JsonUtils.getInstance().readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON 反序列化失败", e);
        }
        return archiveInfo;
    }
}
