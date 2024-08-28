package club.someoneice.test;

import club.someoneice.config.data.Config;

public class TestConfig {
    private static final Config bean = new Config("./test.json5");
    public static final String TEST_STRING = bean.parse("test_string", "TestString0");
    public static final String TEST_STRING1 = bean.parse("test_string1", "TestString1", "This is second test string!");
    public static final int TEST_INT = bean.parse("test_string1", 5, "This is first test int!");

    public static void init() {
        bean.pop();
    }
}
