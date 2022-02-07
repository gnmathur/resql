package com.gmathur.resql.models;

public class ResqlListenerDataModels {
    public static class IntTuple implements ComputedObj {
        private final int _0;
        private final int _1;

        public IntTuple(int _0, int _1) {
            this._0 = _0;
            this._1 = _1;
        }

        public int get_0() { return _0; }
        public int get_1() { return _1; }
    }

    public static class StringWrapper implements ComputedObj {
        private final String s;

        public StringWrapper(String s) {
            this.s = s;
        }

        public String getS() { return s; }
    }

    public static StringWrapper StringWrapperBldr(final String s) {
        return new StringWrapper(s);
    }

    public static IntTuple IntTupleBldr(final int _0, final int _1) {
        return new IntTuple(_0, _1);
    }
}
