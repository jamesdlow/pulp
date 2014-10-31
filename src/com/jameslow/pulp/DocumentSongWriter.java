package com.jameslow.pulp;

import javax.swing.text.*;

public class DocumentSongWriter implements SongWriter {
	private DefaultStyledDocument doc;
	private StyleContext sc;
	
	public static Style getFontFormatToStyle(FontFormat fontformat, StyleContext sc) {
		final Style style = sc.addStyle(null, null);
		style.addAttribute(StyleConstants.Foreground, fontformat.color);
		style.addAttribute(StyleConstants.FontSize, new Integer(fontformat.font.getSize()));
		style.addAttribute(StyleConstants.FontFamily, fontformat.font.getFamily());
		if (fontformat.align == FontFormat.Align.CENTER) {
			//TODO: for some reason everything is align left, even though it gets here
			style.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);
		} else if (fontformat.align == FontFormat.Align.JUSTIFIED) {
			style.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		} else if (fontformat.align == FontFormat.Align.RIGHT) {
			style.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_RIGHT);
		} else if (fontformat.align == FontFormat.Align.LEFT) {
			style.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);
		}
		if (fontformat.font.isBold()) {
			style.addAttribute(StyleConstants.Bold, new Boolean(true));
		}
		if (fontformat.font.isItalic()) {
			style.addAttribute(StyleConstants.Italic, new Boolean(true));
		}
		return style;
	}
	
	public DocumentSongWriter(DefaultStyledDocument doc, StyleContext sc) {
		this.doc = doc;
		this.sc = sc;
	}
	public void write(String line, FontFormat fontformat, boolean newline) {
		try {
			if (newline) {
				line = line + SongList.NL;
			}
			doc.insertString(doc.getLength(), line, getFontFormatToStyle(fontformat,sc));
		} catch (BadLocationException e) {
			//
		}
	}

}
