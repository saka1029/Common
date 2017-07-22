package com.github.saka1029.common.util;

import java.util.HashMap;
import java.util.Map;

public class EnumDecoder<T extends Enum<?> & EnumWithCode> {

    final Map<String, T> map = new HashMap<>();

    public EnumDecoder(T[] values) {
        for (T value : values)
            map.put(value.code(), value);
    }

    public T parse(String code) {
        T result = map.get(code);
        if (result == null)
            throw new IllegalArgumentException("code(" + code + ")が" + map + "の中にありません");
        return result;
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
