package com.github.saka1029.common.util;

import java.util.Iterator;

/**
 * 驟榊縺ョIterator
 */
public class ArrayIterator<T> implements Iterator<T> {

	protected T object[];
	protected int begin, end, current;

	public ArrayIterator(T object[], int begin, int end) {
		this.object = object;
		this.current = this.begin = begin;
		this.end = end;
	}

	public ArrayIterator(T object[]) {
		this(object, 0, object.length);
	}

	/**
	 * 谺。縺ョ隕∫エ　縺後≠繧九°縺ゥ縺°繧定ソ斐☆縲
	 * @return 谺。縺ョ隕∫エ　縺後≠繧九°縺ゥ縺°繧定ソ斐☆縲
	 */
	public boolean hasNext() {
		return current < end;
	}

	/**
	 * 谺。縺ョ隕∫エ　繧定ソ斐☆縲
	 * @return 谺。縺ョ隕∫エ　繧定ソ斐☆縲
	 */
	public T next() {
		return object[current++];
	}

	public void remove()
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
}
