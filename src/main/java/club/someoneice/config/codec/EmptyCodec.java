package club.someoneice.config.codec;


public final class EmptyCodec implements ICodec<Object, Object> {
    public static final EmptyCodec INSTANCE = new EmptyCodec();
    private EmptyCodec() {}

    @Override
    public Object encode(Object o) {
        throw new NullPointerException("This codec does not support encoding.");
    }

    @Override
    public Object decode(Object o) {
        throw new NullPointerException("This codec does not support encoding.");
    }

    @Override
    public String toString() {
        return "EmptyCodec";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyCodec;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
