package com.jameslow.pulp;

public class Chord implements Transposeable {
	private Note bass;
	private Note root;
	private String modifier = "";
	private String creation;
	private boolean parsed = false;
	public static boolean log = false;
	
	public Chord(String chord) {
		this.creation = chord;
		try {
			if (chord.length() > 0 ) {
				String[] rootbass = chord.split("/",2);
				if (rootbass.length > 1) {
					bass = new Note(rootbass[1]);
				} else {
					bass = null;
				}
				if (rootbass[0].length() > 0) {
					if (Theory.isNoteCharacter(rootbass[0].charAt(0))) {
						int i = 1; //Assume first character is letter of the note 
						if (i < rootbass[0].length()) {
							String character = ""+rootbass[0].charAt(i);
							while ((Theory.isFlat(character) || Theory.isSharp(character)) && i < rootbass[0].length()) {
								i++;
								if (i < rootbass[0].length()) {
									character = ""+rootbass[0].charAt(i);
								}
							}
						}
						if (i < rootbass[0].length()) {
							this.modifier = rootbass[0].substring(i);
						}
						this.root = new Note(rootbass[0].substring(0,i));
						if (log) {
							System.out.println(rootbass[0].substring(0,i)+' '+this.modifier);
						}
						parsed = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("DEBUG: Could not parase chord " +chord+ " : " + e.getMessage());
			e.printStackTrace();
		}
	}
	public Chord(String root, String modifier, String bass) {
		this(new Note(root),modifier,new Note(bass));
	}
	public Chord(Note root, String modifier, Note bass) {
		this.root = root;
		this.bass = bass;
		this.modifier = modifier;
		parsed = true;
	}
	public boolean isParsed() {
		return parsed;
	}
	public Note getRootNote() {
		return root;
	}
	public Note getBassNote() {
		return bass;
	}
	public String getChord() {
		return getCreation();
	}
	public String getNormalisedChord() {
		return root.getNormalisedString() + modifier + (bass == null ? "" : "/"+bass.getNormalisedString());
	}
	public String getCreation() {
		if (creation == null) {
			return root.getCreation() + modifier + (bass == null ? "" : "/"+bass.getCreation());
			//return getNormalisedChord();
		} else {
			return creation;
		}
	}
	public Note[] getNotes() {
		//TODO: Implement getNotes()
		return null;
	}
	public String getModifierString() {
		return modifier;
	}
	public Chord transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public Chord transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public Chord transpose(String currentkeystr, String newkeystr) {
		return transpose(currentkeystr,newkeystr,false);
	}
	public Chord transpose(Note currentkey, Note newkey) {
		if (parsed) {
			return new Chord(root.transpose(currentkey, newkey),modifier,(bass == null ? null : bass.transpose(currentkey, newkey)));
		} else {
			return new Chord(getCreation());
		}
	}
	public String toString() {
		return getChord();
	}
}
