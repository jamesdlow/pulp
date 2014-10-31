package com.jameslow.pulp;

import java.io.*;
import java.util.*;

public class Song extends Music {
	private final List<Section> sections = new ArrayList<Section>();
	
	public Song() {
		
	}
	
	public Song(String title) {
		setProperty(SectionType.TITLE,title);
	}
	
	public List<Section> getSections() {
		return sections;
	}
	
	public void addSection(Section section) {
		sections.add(section);
	}
	
	@Override
	public Song copy() {
		Song song = new Song();
		for (SectionProperty prop : properties) {
			song.setProperty(prop.copy());
		}
		return song;
	}
	
	@Override
	protected Song newthis() {
		return new Song();
	}
	
	@Override
	public Song transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public Song transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public Song transpose(String currentkeystr, String newkeystr) {
		return transpose(currentkeystr,newkeystr,false);
	}
	public Song transpose(Note currentkey, Note newkey) {
		Song song = (Song) super.transpose(currentkey, newkey);
		super.transpose(currentkey, newkey);
		for (Section section : sections) {
			song.addSection(section.transpose(currentkey, newkey));
		}
		return song;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(super.toString());
		if (sb.length() > 0) { sb.append("\n"); }
		for (Section section : sections) {
			sb.append(section).append("\n");
		}
		return sb.toString();
	}
	
	public String getFirstLine() {
		for (Section section : sections) {
			String words = section.getFirstLine();
			if (words.length() > 0) {
				return words;
			}
		}
		return "";
	}

	public static Song parse(String filename) throws IOException {
		//TODO: Should we register that this was detected
		String title = new File(filename).getName();
		int pos = title.lastIndexOf(".");
		if (pos >= 0) {
			title = title.substring(0, pos);
		}
		return parse(new BufferedReader(new FileReader(filename)),title);
	}
	
	public static Song parse(final BufferedReader br, String title) throws IOException {
		Song song = new Song();
		String s;
		String last = null;
		Section section = null;
		Line line = null;
		boolean firstchord = true;
		while ((s = br.readLine()) != null) {
			if (isWhitespace(s)) {
				if (section != null && section.getLines().size() > 0) {
					//Do nothing or end current section, if its not empty
					section = null;
				}
			} else if (isTag(s)) {
				if (isInfoTag(s)) {
					//Start new section, or if a info tag do something with it
					Music music;
					if (isSectionTag(last) && section.getLines().size() == 0) {
						//Add to section
						music = section;
					} else {
						//Add to song
						music = song;
					}
					music.setProperty(s);
				} else {
					section = new Section(SectionType.parse(s),s.split(":",2)[0]);
					song.addSection(section);
				}
			} else {
				if (section == null) {
					section = new Section(SectionType.UNKNOWN,SectionType.UNKNOWN.getDefault());
					song.addSection(section);
				}
				//TODO: Somewhere here the thing is starting a new section for 2 chord lines
				if (isChordLine(s)) {
					//Start a new line
					line = new Line(s);
					section.addLine(line);
					if (firstchord && song.getPropertyValue(SectionType.KEY) == null) {
						//If first chord line, assume key for song, unless there is a key
						//TODO: Should we register that this was detected
						song.setProperty(SectionType.KEY, line.getChords().get(0).getCreation());
					}
				} else {
					//Words, start a new line or to current if just chords
					if (isChordLine(last)) {
						line.setWords(s);
					} else {
						line = new Line("",s);
						section.addLine(line);
					}
				}
			}
			last = s;
		}
		if (song.getPropertyValues(SectionType.TITLE).size() == 0) {
			song.setProperty(SectionType.TITLE, title);
		}
		return song;
	}
	
	public static boolean isWhitespace(final String s) {
		return (s == null ? true : s.trim().length() == 0);
	}
	
	//TODO: Improve this so it works for normal chord lines with low space  
	public static boolean isChordLine(final String s) {
		if (isWhitespace(s)) {
			return false;
		} else if (s.length() <= 3) {
			//G,Dm,G7,Eb7
			return true;
		} else if (s.indexOf("#") > 0) {
			return true;
		} else if (s.indexOf("/") > 0) {
			return true;
		} else if (s.indexOf("m7") > 0) {
			return true;
		} else if (s.indexOf("min7") > 0) {
			return true;
		} else if (s.indexOf("maj7") > 0) {
			return true;
		} else if (s.indexOf("sus4") > 0) {
			return true;
		} else if (s.indexOf("sus2") > 0) {
			return true;
		} else if (s.indexOf("sus7") > 0) {
			return true;
		} else if (s.indexOf("b7") > 0) {
			return true;
		} else if (s.indexOf("bm") > 0) {
			return true;
		//covered by above
		//} else if (s.indexOf("bm7") > 0) {
		//	return true;
		} else if (s.indexOf("bmin") > 0) {
			return true;
		} else if (s.indexOf("bmaj") > 0) {
			return true;
		} else if (s.indexOf("bsus") > 0) {
			return true;
		} else if (s.indexOf("minmaj") > 0) {
			return true;
		} else if (s.matches("[^ ]*[A-G]b[ 0-9\\(\\)\\|\\\\$].*")) {
			return true;
		} else if (s.matches("[^ ]*[A-G]\\+[0-9].*")) {
			return true;
		} else if (s.matches("[^ ]*[A-G][b#]*(maj|m|min|-|sus)[b#]*[0-9+]+")) {
			return true;
		} else {
			int n = 0;
			final int l = s.length();
			for (int i = 0; i < l; ++i) {
				if (s.charAt(i) == ' ') {
					n++;
				}
			}
			return n >= l * 2 / 5;
		}
	}
	
	public static boolean isTag(String s) {
		return (isWhitespace(s) ? false : s.indexOf(":") > 0);
	}
	
	public static boolean isSectionTag(String s) {
		return (isWhitespace(s) ? false : isTag(s) && !isInfoTag(s));
	}
	
	public static boolean isInfoTag(String s) {
		return (isWhitespace(s) ? false : isTag(s) && SectionType.parse(s).isInfoTag());
	}
}
