package club.someoneice.config.codec;

public interface ICodec<A, B> {
    B encode(A a);
    A decode(B b);
}
