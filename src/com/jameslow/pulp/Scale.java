package com.jameslow.pulp;

public class Scale implements Transposeable {
	public ScaleIntervals intervals;
	public Note key;
	public String[] cromatic;
	public String[] notes;

	public Scale(String key, ScaleIntervals intervals) {
		this(new Note(key), intervals);
	}
	public Scale(Note key, ScaleIntervals intervals) {
		this.key = key;
		this.intervals = intervals;
		cromatic = Theory.getCromatic(key);
		notes = Theory.getNoteStrings(intervals.getIntIntervals(),cromatic);
	}
	public String[] getNoteStrings() {
		return notes;
	}
	public Scale transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public Scale transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public Scale transpose(String currentkeystr, String newkeystr) {
		return transpose(currentkeystr,newkeystr,false);
	}
	public Scale transpose(Note currentkey, Note newkey) {
		return new Scale(key.transpose(currentkey, newkey),intervals);
	}
}