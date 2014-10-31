package com.jameslow.pulp;

public class LineChord extends Chord {
	private int pos;
	private String othertext = "";
	private boolean isothertext = false;
	private boolean trailingspace = false;
	
	public LineChord(final String chord, final int pos, boolean tailingspace) {
		super(chord);
		setPos(pos);
		setTrailingSpace(tailingspace);
	}
	public LineChord(String root, String modifier, String bass, final int pos, boolean tailingspace) {
		this(new Note(root),modifier,new Note(bass),pos,tailingspace);
	}
	public LineChord(Note root, String modifier, Note bass, final int pos, boolean tailingspace) {
		super(root,modifier,bass);
		setPos(pos);
		setTrailingSpace(tailingspace);
	}
	public int getPos() {
		return pos;
	}
	public void setPos(final int pos) {
		this.pos = pos;
	}
	public LineChord transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public LineChord transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public LineChord transpose(String currentkey, String newkey) {
		return new LineChord(super.transpose(currentkey, newkey).getChord(),pos,trailingspace);
	}
	public LineChord transpose(Note currentkey, Note newkey) {
		return new LineChord(super.transpose(currentkey, newkey).getChord(),pos,trailingspace);
	}
	public void setTrailingSpace(boolean trailingspace) {
		this.trailingspace = trailingspace;
	}
	public boolean getTrailingSpace() {
		return trailingspace;
	}
}