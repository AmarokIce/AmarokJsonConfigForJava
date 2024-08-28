package club.someoneice.config.data;

import club.someoneice.config.codec.ICodec;
import club.someoneice.config.codec.JsonCodecs;
import club.someoneice.json.node.*;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class ConfigHolder<T> {
    private T value;
    protected final String configName;
    protected final Supplier<T> defaultValue;
    protected final ICodec<T, JsonNode<?>> codec;
    protected final JsonNode.NodeType type;
    protected final List<String> comments = new ArrayList<>();

    /**
     * ConfigHolder 是原计划的 ConfigValue 的再实现。负责持有数据，供应默认数据，目标类型的编解码器，接收的数据类型以及注解（Json5限定）。
     * @param configName 类型在 Config 中的名称。
     * @param defaultValue 当持有数据为 Null 或接收目标不匹配时提供的默认数据。默认数据总是具有唯一性。
     * @param codec 从 JsonNode 到对象的编解码器。
     * @param type 期望接收的数据类型，用于在数据类型更变时保证数据精准。
     */
    public ConfigHolder(String configName, Supplier<T> defaultValue, ICodec<T, ? extends JsonNode<?>> codec, JsonNode.NodeType type) {
        this.configName = configName;
        this.defaultValue = defaultValue;
        this.codec = (ICodec<T, JsonNode<?>>) codec;
        this.type = type;
    }

    public ConfigHolder(String configName, T defaultValue, ICodec<T, ? extends JsonNode<?>> codec, JsonNode.NodeType type) {
        this(configName, () -> defaultValue, codec, type);
    }

    public ConfigHolder(String configName, T defaultValue, JsonNode.NodeType type) {
        this(configName, () -> defaultValue, ((ICodec<T, JsonNode<?>>) JsonCodecs.create(type)), type);
    }

    public T get() {
        return value == null ? defaultValue.get() : value;
    }

    public void set(T value) {
        this.value = value == null ? this.defaultValue.get() : value;
    }

    public T reset() {
        this.value = defaultValue.get();
        return this.value;
    }

    public T getDefaultValue() {
        return this.defaultValue.get();
    }

    public void putComments(String ... comments) {
        this.comments.addAll(Arrays.asList(comments));
    }

    public JsonNode.NodeType getType() {
        return type;
    }

    public ICodec<T, ? extends JsonNode<?>> getCodec() {
        return codec;
    }

    public String getConfigName() {
        return configName;
    }

    /**
     * 使用内置编码器取得 JsonNode 可以防止泛型污染问题。<br>
     * @return 目标的 JsonNode 对象数据。
     */
    public JsonNode<?> encode() {
        return codec.encode(this.get());
    }
}
