package com.github.saka1029.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * CSVファイルを読み込むためのReader。
 * 入力可能なファイルは以下の形式に限る。
 * (1)区切り文字で区切られた複数の項目から構成される。
 * (2)各項目を囲む引用符はない。
 * (3)各項目の値として区切り文字自身を含まない。
 * 区切り文字はカンマが規定値であるが、setSeparator()で変更することができる。
 * 改行コードとしてはCR, LF, CR+LFの３種類を許容する。
 * 同一ファイル内でこれらが混在していてもよい。
 * ファイル末尾のレコードの終端には、改行はあってもなくてもよい。
 * スペースはフィールドの一部とみなし無視することはない。
 *
 */
public class CSVFastReader extends Reader implements FieldsReader {

	private static final int DEFAULT_BUFFER_SIZE = 8192;

	private BufferedReader input;

	public CSVFastReader(BufferedReader bufferedReader) {
		input = bufferedReader;
	}

	public CSVFastReader(Reader reader) {
		this(new BufferedReader(reader, DEFAULT_BUFFER_SIZE));
	}

	public CSVFastReader(InputStream inputStream, String encoding)
		throws UnsupportedEncodingException {
		this(new InputStreamReader(inputStream, encoding));
	}

	public CSVFastReader(String path)
		throws FileNotFoundException {
		this(new FileReader(path));
	}

	public CSVFastReader(String path, String encoding)
		throws UnsupportedEncodingException, FileNotFoundException {
		this(new FileInputStream(path), encoding);
	}

	public CSVFastReader(File file)
		throws FileNotFoundException {
		this(new FileReader(file));
	}

	public CSVFastReader(File file, String encoding)
		throws UnsupportedEncodingException, FileNotFoundException {
		this(new FileInputStream(file), encoding);
	}

	@Override
	public void close() throws IOException {
		input.close();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return input.read(cbuf, off, len);
	}

	/**
	 * 項目の区切り文字を正規表現で指定します。
	 */
	private String separator = ",";

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	private String quote = null;

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	/**
	 * １行分のデータを読み込み、カンマで区切って文字列のリストとして返す。
	 * @return 読み取ったフィールドのリスト
	 * @throws IOException
	 */
	public Fields readFields() throws IOException {
		String line = input.readLine();
		if (line == null) return null;
		// String.splitは第２引数で負の数を指定しないと末尾の連続したseparatorを無視する。
		String[] items = line.split(separator, -1);
		Fields fields = new ArrayFields(items);
		return fields;
	}

	@Override
	public boolean ready() throws IOException {
		return input.ready();
	}

}
