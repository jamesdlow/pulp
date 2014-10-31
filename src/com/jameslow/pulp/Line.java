package com.jameslow.pulp;

import java.util.*;
import java.util.regex.*;

public class Line implements Transposeable {
	//TODO: could always tweak regular expression
	//"[^\f\n\r\t\v\x85\p{Z}]"
	//private static final Pattern CHORD = Pattern.compile("\\S+");
	//private static final Pattern CHORD = Pattern.compile("\\||\\(|\\)|\\S+");
	//private static final Pattern CHORD = Pattern.compile("\\||\\(|\\)|[a-zA-Z_0-9/#]+");
	private static final Pattern CHORD = Pattern.compile("\\(|\\)|\\||\\\\|[a-zA-Z_0-9/#\\+\\-]+");
	
	private String words = "";
	private final List<LineChord> chords = new ArrayList<LineChord>();
	
	public Line() {

	}
	
	public Line(final String chordstring) {
		final Matcher m = CHORD.matcher(chordstring);
		while (m.find()) {
			boolean space = false;
			if (m.end() < chordstring.length()) {
				if (" ".compareTo(""+chordstring.charAt(m.end())) == 0) {
					space = true;
				}
			}
			chords.add(new LineChord(m.group(),m.start(),space));
		}
	}
	public Line(final String chordstring, final String words) {
		this(chordstring);
		this.words = words;
	}

	public String getWords() {
		return words;
	}

	public void setWords(final String words) {
		this.words = words;
	}
	
	public void addChord(LineChord chord) {
		chords.add(chord);
	}

	public List<LineChord> getChords() {
		return chords;
	}
	public Line transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public Line transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public Line transpose(String currentkeystr, String newkeystr) {
		return transpose(currentkeystr,newkeystr,false);
	}
	public Line transpose(Note currentkey, Note newkey) {
		Line line = new Line();
		line.setWords(words);
		for(LineChord chord : chords) {
			line.addChord(chord.transpose(currentkey,newkey));
		}
		return line;
	}
	
	@Override
	public String toString() {
		if (chords.isEmpty()) {
			return words;
		}
		final StringBuilder sb = new StringBuilder();
		int p = 0;
		for (LineChord chord : chords) {
			while (p < chord.getPos() || (sb.length() > 0 && " ".compareTo(""+sb.charAt(sb.length()-1)) != 0)) {
				sb.append(" ");
				p++;
			}
			sb.append(chord.getChord());
			p += chord.getCreation().length();
		}
		return ((words != null && words.length() > 0) ? sb.append('\n').append(words) : sb).toString();
	}
}