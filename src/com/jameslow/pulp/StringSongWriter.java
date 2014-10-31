package com.jameslow.pulp;

public class StringSongWriter implements SongWriter {
	private StringBuffer text = new StringBuffer();
	
	public void write(String line, FontFormat fontformat, boolean newline) {
		if (newline) {
			line = line + SongList.NL;
		}
		text.append(line);
	}
	public String getText() {
		return text.toString();
	}
}
