package com.jameslow.pulp;

public class Note implements Transposeable {
	private int index;
	private String creation;
	
	public Note(String note) {
		this(Theory.getLetter(note));
		creation = note;
	}
	public Note(int index) {
		this.index = index;
	}
	public String getString() {
		return getCreation();
	}
	public String getNormalisedString() {
		return getString(false);
	}
	public String getString(boolean sharps) {
		return Theory.getLetter(index,sharps);
	}
	public String getString(Note key) {
		return Theory.getLetter(index,key);
	}
	public String getCreation() {
		if (creation == null) {
			return getNormalisedString();
		} else {
			return creation;
		}
	}
	public boolean isSharp() {
		return Theory.isSharpNote(getCreation());
	}
	public boolean isFlat() {
		return Theory.isFlatNote(getCreation());
	}
	public int getIndex() {
		return index;
	}
	public boolean equal(Note note, boolean quick) {
		if (quick) {
			return this.getIndex() == note.getIndex();
		} else {
			return equal(note);
		}
	}
	public boolean equal(Note note) {
		return this.getIndex() == note.getIndex() && this.isSharp() == note.isSharp() && this.isFlat() == note.isFlat();
	}
	public Note transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public Note transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public Note transpose(String currentkeystr, String newkeystr) {
		return transpose(currentkeystr,newkeystr,false);
	}
	public Note transpose(Note currentkey, Note newkey) {
		return new Note(Theory.getLetter(index + Theory.getInterval(currentkey,newkey), newkey));
	}
}
