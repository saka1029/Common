package com.github.saka1029.common.io;

/**
 * 読み取り専用の文字列の配列です。
 *
 */
public interface Fields extends Iterable<String> {

	int size();

	String get(int index);

}
