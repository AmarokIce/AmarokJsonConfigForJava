package club.someoneice.config.codec;

import club.someoneice.json.node.*;

import java.util.List;
import java.util.Map;

public final class JsonCodecs {
    public static final EmptyCodec EMPTY = EmptyCodec.INSTANCE;

    public static final Codec<String, StringNode> STRING_CODEC = new Codec<>(StringNode::new, StringNode::getObj);
    public static final Codec<Integer, IntegerNode> INT_CODEC = new Codec<>(IntegerNode::new, IntegerNode::getObj);
    public static final Codec<Long, LongNode> LONG_CODEC = new Codec<>(LongNode::new, LongNode::getObj);
    public static final Codec<Float, FloatNode> FLOAT_CODEC = new Codec<>(FloatNode::new, FloatNode::getObj);
    public static final Codec<Double, DoubleNode> DOUBLE_CODEC = new Codec<>(DoubleNode::new, DoubleNode::getObj);
    public static final Codec<Boolean, BooleanNode> BOOLEAN_CODEC = new Codec<>(BooleanNode::new, BooleanNode::getObj);
    public static final Codec<List<JsonNode<?>>, ArrayNode> ARRAY_CODEC = new Codec<>(ArrayNode::new, ArrayNode::getObj);
    public static final Codec<Map<String, JsonNode<?>>, MapNode> MAP_CODEC = new Codec<>(MapNode::new, MapNode::getObj);

    /**
     * Codec 自动推断，通过 {@link JsonNode.NodeType} 进行筛选。不支持使用 Null、Other 分支。
     * @throws IllegalArgumentException 当分支为 Null、Other 时抛出。
     * @param type 输入的 JsonNode.NodeType.
     * @return 通过 Type 筛选的合适地编解码器，通常需要需要在取得返回值后强制转型。
     */
    public static Codec<?, ? extends JsonNode<?>> create(JsonNode.NodeType type) {
        Codec<?, ? extends JsonNode<?>> codec;
        switch (type) {
            case String: return STRING_CODEC;
            case Int: return INT_CODEC;
            case Long: return LONG_CODEC;
            case Float: return FLOAT_CODEC;
            case Double: return DOUBLE_CODEC;
            case Boolean: return BOOLEAN_CODEC;
            case Array: return ARRAY_CODEC;
            case Map: return MAP_CODEC;
            default: throw new IllegalArgumentException("Codec deduce does not support " + type);
        }
    }
}
