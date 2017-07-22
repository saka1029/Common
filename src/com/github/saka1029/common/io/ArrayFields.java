package com.github.saka1029.common.io;

import java.util.Iterator;

import com.github.saka1029.common.util.ArrayIterator;


public class ArrayFields implements Fields {

	private String[] fields;

	public ArrayFields(String[] fields) {
		this.fields = fields;
	}

	public ArrayFields(Fields fields) {
		this.fields = new String[fields.size()];
		for (int i = 0, size = fields.size(); i < size; ++i)
			this.fields[i] = fields.get(i);
	}

	public String get(int index) {
		return fields[index];
	}

	public void set(int index, String value) {
		this.fields[index] = value;
	}

	public int size() {
		return fields.length;
	}

	public Iterator<String> iterator() {
		return new ArrayIterator<String>(fields);
	}

}
