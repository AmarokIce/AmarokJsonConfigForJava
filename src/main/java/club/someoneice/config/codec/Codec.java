package club.someoneice.config.codec;

import java.util.function.Function;

public class Codec<A, B> implements ICodec<A, B> {
    protected final Function<A, B> encoderFunction;
    protected final Function<B, A> decoderFunction;

    public Codec(Function<A, B> encode, Function<B, A> decode) {
        this.encoderFunction = encode;
        this.decoderFunction = decode;
    }

    @Override
    public B encode(A a) {
        return encoderFunction.apply(a);
    }

    @Override
    public A decode(B b) {
        return decoderFunction.apply(b);
    }
}
