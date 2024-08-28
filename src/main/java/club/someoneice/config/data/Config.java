package club.someoneice.config.data;

import club.someoneice.config.codec.JsonCodecs;
import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.Json5Builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public final class Config {
    public final String configPath;
    public final MapNode configNode;
    public final ArrayList<ConfigHolder<?>> configHolders = new ArrayList<>();
    private final boolean isJson5;

    public Config(final String configName) {
        this.configPath = configName;
        this.configNode = parseOrLoadFile(configName);
        this.isJson5 = configName.endsWith(".json5");
    }

    public String parse(String configName, String defaultValue, String ... comments) {
        ConfigHolder<String> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.STRING_CODEC, JsonNode.NodeType.String);
        holder.putComments(comments);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public int parse(String configName, int defaultValue, String ... comments) {
        ConfigHolder<Integer> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.INT_CODEC, JsonNode.NodeType.Int);
        holder.putComments(comments);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public long parse(String configName, long defaultValue, String ... comments) {
        ConfigHolder<Long> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.LONG_CODEC, JsonNode.NodeType.Long);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public float parse(String configName, float defaultValue, String ... comments) {
        ConfigHolder<Float> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.FLOAT_CODEC, JsonNode.NodeType.Float);
        holder.putComments(comments);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public double parse(String configName, double defaultValue, String ... comments) {
        ConfigHolder<Double> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.DOUBLE_CODEC, JsonNode.NodeType.Double);
        holder.putComments(comments);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public boolean parse(String configName, boolean defaultValue, String ... comments) {
        ConfigHolder<Boolean> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.BOOLEAN_CODEC, JsonNode.NodeType.Boolean);
        holder.putComments(comments);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public List<JsonNode<?>> parse(String configName, List<JsonNode<?>> defaultValue, String ... comments) {
        ConfigHolder<List<JsonNode<?>>> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.ARRAY_CODEC, JsonNode.NodeType.Array);
        holder.putComments(comments);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public Map<String, JsonNode<?>> parse(String configName, Map<String, JsonNode<?>> defaultValue, String ... comments) {
        ConfigHolder<Map<String, JsonNode<?>>> holder = new ConfigHolder<>(configName, defaultValue, JsonCodecs.MAP_CODEC, JsonNode.NodeType.Map);
        holder.putComments(comments);
        this.configHolders.add(parse(holder));
        return holder.get();
    }

    public <T> T parseCustom(ConfigHolder<T> holder, String ... comments) {
        this.configHolders.add(parse(holder));
        holder.putComments(comments);
        return holder.get();
    }

    public void pop() {
        Json5Builder builder = new Json5Builder();
        Json5Builder.ObjectBean base = builder.getObjectBean();

        for (ConfigHolder<?> holder : this.configHolders) {
            JsonNode<?> node = holder.encode();

            if (!isJson5 || holder.comments.isEmpty()) {
                base.put(holder.getConfigName(), node);
                continue;
            }

            base.enterLine();
            for (String comment : holder.comments) {
                base.addNote(comment);
            }
            base.put(holder.getConfigName(), node);
        }

        builder.put(base);

        try {
            Files.write(Paths.get(this.configPath), builder.build().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> ConfigHolder<T> parse(ConfigHolder<T> holder) {
        if (!this.configNode.has(holder.configName)) {
            return holder;
        }

        JsonNode<?> node = this.configNode.get(holder.configName).asTypeNode();
        if (!node.typeOf(holder.type)) {
            return holder;
        }

        holder.set(holder.codec.decode(node));
        return holder;
    }

    private static MapNode parseOrLoadFile(String fileName) {
        JSON json = JSON.json5;

        File file = new File(fileName);
        if (!file.exists() && !file.isFile()) {
            try {
                if ((!file.getParentFile().exists() && !file.getParentFile().mkdirs()) || !file.createNewFile()) {
                    throw new RuntimeException("Failed to parse config file: " + fileName);
                }

                Files.write(file.toPath(), "{}".getBytes());

                return new MapNode(new LinkedHashMap<>());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (!file.getName().endsWith(".json") && !file.getName().endsWith(".json5")) {
            throw new RuntimeException("The file is not a json file: " + fileName);
        }

        return json.tryPullObjectOrEmpty(json.parse(file));
    }
}
