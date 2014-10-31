package com.jameslow.pulp;

import java.awt.*;

public class Format {
	public boolean display = true;
	private FontFormat wordfont = new FontFormat();
	private FontFormat titlefont = null;
	private FontFormat chordfont = null;
	private FontFormat whitespacefont = null;
	public int spaceindent = 0;
	public int tabindent = 0;
	public boolean displayblank = false;
	public boolean displaytitlecolon = true;
	public boolean displaytitlespace = false;
	public boolean displaycount = false;
	public boolean doubleline = false;
	public boolean displayfirstchordsonly = true;
	public boolean propertiessameline = false;
	
	public FontFormat getWordFont() {
		return wordfont;
	}
	public void setWordFont(FontFormat font) {
		wordfont = font;
	}
	public FontFormat getTitleFont() {
		return (titlefont == null ? wordfont : titlefont);
	}
	public void setTitleFont(FontFormat font) {
		titlefont = font;
	}
	public FontFormat getChordFont() {
		return (chordfont == null ? wordfont : chordfont);
	}
	public void setChordFont(FontFormat font) {
		chordfont = font;
	}
	public FontFormat getWhiteSpaceFont() {
		return (whitespacefont == null ? wordfont : whitespacefont);
	}
	public void setWhiteSpaceFont(FontFormat font) {
		whitespacefont = font;
	}
}
