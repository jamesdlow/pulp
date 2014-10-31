package com.jameslow.pulp;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.text.*;

public class SongList {
	private int columns = 2;
	private boolean wrapsongs = true;
	private boolean showunknown = false;
	private Format defaultformat;
	private Map<SectionType,Format> sectionformats;
	public final static String NL = "\r\n";
	
	public SongList() {
		this(new HashMap<SectionType,Format>());
	}
	public SongList(Map<SectionType,Format> sectionformats) {
		setFormats(sectionformats);
	}
	public Map<SectionType,Format> getFormats() {
		return sectionformats;
	}
	public void setFormats(Map<SectionType,Format> sectionformats) {
		this.sectionformats = sectionformats;

	}
	public Format getDefaultFormat() {
		if (defaultformat == null) {
			if (sectionformats.containsKey(SectionType.VERSE)) {
				return sectionformats.get(SectionType.VERSE);
			} else {
				return new Format();
			}
		} else {
			return defaultformat;
		}
	}
	public void setDefaultFormat(Format format) {
		defaultformat = format;
	}
	public Format getFormat(SectionType sectiontype) {
		Format format;
		if (sectionformats.containsKey(sectiontype)) {
			format = sectionformats.get(sectiontype);
		} else {
			format = getDefaultFormat();
		}
		return format;
	}
	public void setFormat(SectionType sectiontype, Format format) {
		sectionformats.put(sectiontype, format);
	}
	private String getWhiteSpace(Format format) {
		StringBuffer whitespace = new StringBuffer();
		for (int i = 0; i < format.tabindent; i++) {
			whitespace.append("\t");
		}
		for (int i = 0; i < format.spaceindent; i++) {
			whitespace.append(" ");
		}
		return whitespace.toString();
	}
	public void writeSong(Song song, SongWriter writer) {
		//TODO: Setting for line feed / carriage return
		for (SectionProperty prop : song.getProperties()) {
			Format format = getFormat(prop.type);
			String whitespace = getWhiteSpace(format);
			if (format.display) {
				if ((format.getWordFont().display && prop.data.length() > 0) || (format.getTitleFont().display && prop.getTitle().length() > 0)) {
					writer.write(whitespace, format.getWhiteSpaceFont(), false);
					if (format.getTitleFont().display && prop.getTitle().length() > 0) {
						writer.write(prop.getTitle()+(format.displaytitlecolon ? ":" : "")+(format.displaytitlespace ? " " : ""), format.getTitleFont(), false);
					}
					if (format.getWordFont().display && prop.data.length() > 0) {
						writer.write(prop.data, format.getWordFont(), false);
					}
					writer.write("", format.getWordFont(), true);
				}
			}
		}
		int i = 0;
		for (Section section: song.getSections()) {
			Format format = getFormat(section.getType());
			if (format.display) {
				String whitespace = getWhiteSpace(format);
				if (format.getTitleFont().display && section.getTitle().length() > 0) {
					writer.write(whitespace, format.getWhiteSpaceFont(), false);
					writer.write(section.getTitle()+(format.displaytitlecolon ? ":" : ""), format.getTitleFont(), true);
				}
				for (Line line: section.getLines()) {
					StringBuffer chordsline = new StringBuffer("");
					StringBuffer wordsline = new StringBuffer("");
					int chordindex = 0;
					int wordpos = 0;
					java.util.List<LineChord> chords = line.getChords();
					String words = line.getWords();
					while (chordindex < chords.size() || wordpos < words.length()) {
						LineChord chord = null;
						if (chordindex < chords.size()) {
							chord = chords.get(chordindex);
						}
						while (wordpos < words.length() && (chord == null || wordpos < chord.getPos())) {
							wordsline.append(words.charAt(wordpos));
							wordpos++;
						}
						if (chord != null) {
							//System.out.println(""+wordpos + " " + words.length() + "; " + chordsline.length() + " " + chord.getPos() + "; " +  getLength(font,wordsline.toString()) + " " + getLength(font,chordsline.toString()));
							//System.out.println(""+(wordpos >= words.length()) + "; " + (chordsline.length() < chord.getPos()) + "; " + (getLength(font,wordsline.toString()) > getLength(font,chordsline.toString())));
							while ((wordpos >= words.length() && chordsline.length() < chord.getPos()) || getLength(format.getWordFont().font,wordsline.toString()) > getLength(format.getChordFont().font,chordsline.toString())) {
								chordsline.append(" ");
							}
							//System.out.println(chordsline+ "; " + chord);
							chordsline.append(chord);
							chordindex++;
							if (chordindex < chords.size()) {
								LineChord nextchord = chords.get(chordindex);
								//This works correctly, but in some circumstances you still get a space after a bracket, because of trying to put the next chord in the right place
								if (chord.getTrailingSpace() || (chord.isParsed() && nextchord.isParsed())) {
									chordsline.append(" ");
								}
								chord = nextchord;
							} else {
								chord = null;
							}
						}
					}
					if (format.doubleline) {
						writer.write("", format.getWordFont(), true);
					}
					if (chordsline.length() > 0 || format.displayblank) {
						writer.write(whitespace, format.getWhiteSpaceFont(), false);
						writer.write(chordsline.toString(), format.getChordFont(), true);
					}
					if (wordsline.length() > 0 || format.displayblank) {
						writer.write(whitespace, format.getWhiteSpaceFont(), false);
						writer.write(wordsline.toString(), format.getWordFont(), true);
					}
				}
			}
			i++;
			if (i < song.getSections().size()) {
				writer.write("", format.getWordFont(), true);
			}
		}
	}
	public static double getLength(Component component, String text) {
		return getLength(component.getGraphics(),text);
	}
	public static double getLength(Graphics graphics, String text) {
		FontMetrics metrics = graphics.getFontMetrics();
		Rectangle2D rect = metrics.getStringBounds(text, graphics);
		return rect.getWidth();
	}
	public static double getLength(Font font, String text) {
		AffineTransform tx = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(tx,false,true);
		Rectangle2D rect = font.getStringBounds(text, frc);
		return rect.getWidth();
	}
}
