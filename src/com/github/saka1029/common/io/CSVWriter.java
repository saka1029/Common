package com.github.saka1029.common.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class CSVWriter extends Writer implements FieldsWriter {

	private static final char QUOTE = '\"';

	private Writer output;
	private String newLine = System.getProperty("line.separator");
	private StringBuilder sb = new StringBuilder();

	public CSVWriter(Writer writer) {
		this.output = writer;
	}

	public CSVWriter(OutputStream outputStream, String encoding)
		throws UnsupportedEncodingException {
		this(new OutputStreamWriter(outputStream, encoding));
	}

	public CSVWriter(String path)
		throws IOException {
		this(new FileWriter(path));
	}

	public CSVWriter(String path, String encoding)
		throws UnsupportedEncodingException, FileNotFoundException {
		this(new FileOutputStream(path), encoding);
	}

	public CSVWriter(File file)
		throws IOException {
		this(new FileWriter(file));
	}

	public CSVWriter(File file, String encoding)
		throws UnsupportedEncodingException, FileNotFoundException {
		this(new FileOutputStream(file), encoding);
	}

	public String getNewLine() {
		return newLine;
	}

	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		output.write(cbuf, off, len);
	}

	public void flush() throws IOException {
		output.flush();
	}

	private String quote(String s) {
		sb.setLength(0);
		sb.append(QUOTE);
		if (s != null) {
			for (int i = 0, size = s.length(); i < size; ++i) {
				char ch = s.charAt(i);
				if (ch == QUOTE) sb.append(ch);
				sb.append(ch);
			}
		}
		sb.append(QUOTE);
		return sb.toString();
	}

	public void writeFields(Fields fields) throws IOException {
		boolean first = true;
		for (String field : fields) {
			if (first)
				first = false;
			else
				output.write(",");
			output.write(quote(field));
		}
		output.write(newLine);
	}

}
