package com.jameslow.pulp;

import java.util.*;

public class Section extends Music implements Transposeable {
	private SectionType type;
	private int number;
	private String creation;
	private List<Line> lines = new ArrayList<Line>();
	
	public Section(final SectionType type, String creation) {
		setType(type);
		this.creation = creation;
	}
	
	public Section(final SectionType type, String creation, List<Line> lines) {
		this(type,creation);
		setLines(lines);
	}
	
	public Section(final SectionType type, String creation, List<Line> lines , final int number) {
		this(type,creation,lines);
		setNumber(number);
	}

	public SectionType getType() {
		return type;
	}

	public void setType(final SectionType type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public List<Line> getLines() {
		return lines;
	}
	
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
	
	public void addLine(Line line) {
		lines.add(line);
	}
	
	public String getTitle() {
		return (type == SectionType.UNKNOWN ? creation : type.getDefault());
	}
	
	public String getFirstLine() {
		if (!type.isInfoTag()) {
			for (Line line : lines) {
				String words = line.getWords();
				if (words != null && words.trim().length() > 0) {
					return words;
				}
			}
		}
		return "";
	}
	
	@Override
	public Section copy() {
		Section section = new Section(type,creation,lines,number);
		for (SectionProperty prop : properties) {
			section.setProperty(prop.copy());
		}
		return section;
	}
	@Override
	protected Music newthis() {
		return new Section(type, creation);
	}
	
	@Override
	public Section transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public Section transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public Section transpose(String currentkeystr, String newkeystr) {
		return transpose(new Note(currentkeystr),new Note(newkeystr));
	}
	public Section transpose(Note currentkey, Note newkey) {
		String keystring = getPropertyValue(SectionType.KEY);
		if (keystring != null) {
			Note sectionkey = new Note(keystring);
			newkey = sectionkey.transpose(currentkey,newkey);
			currentkey = sectionkey;
		}
		Section section = (Section)super.transpose(currentkey, newkey);
		for (Line line : lines) {
			section.addLine(line.transpose(currentkey, newkey));
		}
		return section;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder((type == SectionType.UNKNOWN ? "" : type.getDefault() + ":\n"));
		//TODO: Sort out number business
		if (number > 0) {
			sb.append(' ').append(number);
		}
		sb.append(super.toString());
		for (Line line : lines) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}
}