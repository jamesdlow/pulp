package com.jameslow.pulp;

public enum SectionType {
	UNKNOWN (false,true),
	INTRO (false,true),
	VERSE (false,true),
	CHORUS (false,true),
	PRECHORUS (false,true),
	REFRAIN (false,true),
	BRIDGE (false,true),
	OUTRO (false,true),
	INSTRUMENTAL (false,true),
	RIFF (false,true),
	
	TITLE (true,false),
	DESCRIPTION (true,false),
	COPYRIGHT (true,false),
	AUTHOR (true,false),
	DATE (true,false),
	CCLI (true,false),
	ALBUM (true,false),
	ARTIST (true,false),
	TRACK (true,false),
	ALBUMARTIST (true,false),
	KEY (true,true),
	ALTKEY (true,false), //At the moment not transposing
	YOUTUBE(true,false),
	HIGHNOTE(true,true),
	CAPOKEY(true,false),
	CAPOPOSITION(true,false);
	
	private String[] sectionstrings;
	private boolean infotag;
	private boolean transposeable;
	
	private SectionType(boolean infotag, boolean transposeable) {
		sectionstrings = getSectionStrings(name().toLowerCase());
		this.infotag = infotag;
		this.transposeable = transposeable;
	}
	private String[] getSectionStrings(String key) {
		return Theory.readPropertyArray("section." + key);
	}
	public String getDefault() {
		return sectionstrings[0];
	}
	public boolean isInfoTag() {
		return infotag;
	}
	public boolean isTransposeable() {
		return transposeable;
	}
	public static SectionType parse(String line) {
		SectionType[] sections = SectionType.values();
		for (int i=0; i<sections.length; i++) {
			SectionType section = sections[i];
			for (int j=0; j<section.sectionstrings.length; j++) {
				if (line.toUpperCase().startsWith(section.sectionstrings[j].toUpperCase())) {
					return section;
				}
			}
		}
		return UNKNOWN;
	}
}
