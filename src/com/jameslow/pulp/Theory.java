package com.jameslow.pulp;

import java.util.ResourceBundle;

/**
 * For anything humanly redable 1 = first note in a scale etc, internally though 0 = first note, 1st degree
 * C = 0, C# = 1, etc.
 * String interval: 1,#1,2,etc.
 * int Interval: 0,1,2,etc.
 * String index: 1,2,3,4,5,6,7 (no sharps or flats)
 * Note note: Note class
 * String note: C,C#,etc.
 * @author James
 *
 */
public class Theory {
	public static final String[] NOTES_SHARPS = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	public static final String[] NOTES_FLATS = {"C","Db","D","Eb","E","F","Gb","G","Ab","A","Bb","B"};
	public static final int[] NOTES_CYCLE_MAP = {0,7,2,9,4,11,6,1,8,3,10,5};
	
	public static final String[] SCALES_SHARPS = {"1","#1","2","#2","3","4","#4","5","#5","6","#6","7"};
	public static final String[] SCALES_FLATS = {"1","b2","2","b3","3","4","b5","5","b6","6","b7","7"};
	public static final String[] SCALES_ALT1 = {"1","#1","2","#2","3","4","#4","5","#5","6","dom7","maj7"};
	
	public static final String[] CYCLE_KEYS_PRIMARY = {"C","G","D","A","E","B","Gb","Db","Ab","Eb","Bb","F"};
	public static final String[] CYCLE_KEYS_SHARPS = {"C","G","D","A","E","B","F#","C#","G#","D#","A#","F"};
	public static final String[] CYCLE_FITHS = {"","F#","C#","G#","D#","A#","E#","B#","F##","C##","G##","D##"};
	public static final String[] CYCLE_FORTHS = {"","Bb","Eb","Ab","Db","Gb","Cb","Fb","Bbb","Ebb","Abb","Dbb"};
	public static final String[] CYCLE_KEYS_FLATS = {"C","F","Bb","Eb","Ab","Cb","Gb","","G#","D#","A#","F"};
	
	public static final String[] SOLFEGE_SHARPS = {"Do","Di","Re","Ri","Mi","Fa","Fi","Sol","Sil","La","Li","Ti"};
	public static final String[] SOLFEGE_FLATS = {"Do","Ra","Re","Me","Mi","Fa","Sal","Sol","Le","La","Ta","Ti"};

	public static final String THEORY_BUNDLE = "theory";
	protected static final ResourceBundle scales = ResourceBundle.getBundle(THEORY_BUNDLE);
	
	public static void main(String args[]) {
		System.out.println("Test");
		String[] intervals = readStringIntervals("pentatonic.minor");
		Scale scale = new Scale("A",new ScaleIntervals(intervals));
		for(int i=0; i<intervals.length; i++) {
			String interval = intervals[i];	
			System.out.print(interval + ", ");
			System.out.print(getInterval(interval) + ", ");
			String note = scale.getNoteStrings()[i];
			System.out.print(note + ", ");
			System.out.println(getLetter(note));
		}
		/*
		//Chord chord = new Chord("E");
		Chord chord = new Chord("F#min7/F");
		System.out.println(chord.getCreation());
		System.out.println(chord.getNormalisedChord());
		System.out.println(chord.getRootNote().getString());
		System.out.println(chord.getModifierString());
		System.out.println(chord.getBassNote().getString());
		*/
		//System.out.println((new Note("F#")).transpose(new Note("A"), new Note("B")).getCreation());
		System.out.println((new Chord("F#m7")).transpose(new Note("A"), new Note("B")).getChord());
		System.out.println((new Chord("F#m")).transpose(new Note("A"), new Note("B")).getChord());
		//System.out.println((new Note("A")).transpose(new Note("E"), new Note("F")).getCreation());
		//System.out.println((new Chord("Amaj7")).transpose(new Note("E"), new Note("F")).getChord());
		//System.out.println((new Chord("|")));
		//System.out.println((new Chord("(")));
		//System.out.println((new Chord(")")));
	}
	
	//
	// Music theory functions
	//
	protected static String normaliseInterval(String index) {
		return ""+normalise(Integer.parseInt(index),1,7);
	}
	protected static int normaliseInterval(int interval) {
		return normalise(interval,0,11);
	}
	public static int getInterval(Note note1, Note note2) {
		return note2.getIndex() - note1.getIndex();
	}
	public static int getInterval(String note1, String note2) {
		return getInterval(new Note(note1),new Note(note2));
	}
	protected static int getIntervalIndex(String index) {
		int intValue = Integer.parseInt(index);
		int count = 1;
		while (intValue > 7) {
			intValue = intValue - 7;
			count++;
		}
		return getIndex(""+intValue,SCALES_SHARPS);
	}
	protected static int getSharpFlats(String note_interval) {
		int sharpflats = 0;
		for(int i=0; i<note_interval.length(); i++) {
			String element = ""+note_interval.charAt(i);
			if (isFlat(element)) {
				sharpflats--;
			} else if (isSharp(element)) {
				sharpflats++;
			}
		}
		return sharpflats;
	}
	protected static int getInterval(String interval) {
		int i = 0;
		String element = ""+interval.charAt(i);
		while (!isInteger(element) && i < interval.length()-1) {
			i++;
			element = ""+interval.charAt(i);
		}
		if (i <= 0) {
			return getIntervalIndex(interval);
		} else {
			return getIntervalIndex(interval.substring(i)) + getSharpFlats(interval.substring(0,i));
		}
		/*
		for(int i=0; i<interval.length(); i++) {
			String element = ""+interval.charAt(i);
			if (isInteger(element)) {
				return getIntervalIndex(interval.substring(i)) + sharpflats;
			} else if (isFlat(element)) {
				sharpflats--;
			} else if (isSharp(element)) {
				sharpflats++;
			}
		}
		return sharpflats;
		*/
	}
	protected static String getInterval(int interval) {
		return SCALES_FLATS[interval];
	}
	protected static int[] getIntervals(String[] intervals) {
		int[] result = new int[intervals.length];
		for(int i=0; i<intervals.length; i++) {
			result[i] = getInterval(intervals[i]);
		}
		return result;
	}
	protected static String[] getIntervals(int[] intervals) {
		String[] result = new String[intervals.length];
		for(int i=0; i<intervals.length; i++) {
			result[i] = getInterval(intervals[i]);
		}
		return result;
	}
	protected static String[] getMode(String[] intervals, int degree) {
		int[] mode = getMode(getIntervals(intervals),degree);
		return getIntervals(mode);
	}
	protected static int[] getMode(int[] intervals, int degree) {
		int[] result = new int[intervals.length];
		int start = intervals[degree-1];
		for (int i=0; i<intervals.length; i++) {
			result[i] = normaliseInterval(intervals[i]-start);
		}
		return rotateArray(result,-(degree-1));
	}
	protected static int getNoteIndex(String index) {
		return getIndex(index,NOTES_SHARPS);
	}
	protected static int normaliseNote(int note) {
		return normalise(note,0,11);
	}
	protected static int getLetter(String note) {
		int initial = getNoteIndex(""+note.charAt(0));
		int sharpsflats = 0;
		if (note.length() > 1) {
			sharpsflats = getSharpFlats(note.substring(1));
		}
		return initial + sharpsflats;
	}
	protected static String getLetter(int note, boolean sharps) {
		note = normaliseNote(note);
		if (sharps) {
			return NOTES_SHARPS[note];
		} else {
			return NOTES_FLATS[note];
		}
	}
	protected static String getLetter(int note, Note key) {
		return Theory.getLetter(normaliseNote(note),isSharpKey(key));
	}
	protected static String[] getNoteStrings(int[] intervals, String[] cromatic) {
		String[] notes = new String[intervals.length];
		for(int i=0; i<intervals.length; i++) {
			notes[i] = cromatic[intervals[i]];
		}
		return notes;
	}
	protected static int[] getNoteInts(int[] intervals, Note key) {
		//TODO: Check this works
		int[] notes = new int[intervals.length];
		for(int i=0; i<intervals.length; i++) {
			notes[i] = normaliseNote(intervals[i]-key.getIndex());
		}
		return notes;
	}
	public static String[] getCromatic(Note note) {
		return rotateArray(NOTES_SHARPS,-note.getIndex());
	}

	//
	// Utility type funtions
	//
	private static int[] rotateArray(int[] array, int shift) {
		int[] result = new int[array.length];
		for (int i=0; i<array.length; i++) {
			result[normalise(i+shift,0,array.length-1)] = array[i];
		}
		return result;
	}
	private static String[] rotateArray(String[] array, int shift) {
		String[] result = new String[array.length];
		for (int i=0; i<array.length; i++) {
			result[normalise(i+shift,0,array.length-1)] = array[i];
		}
		return result;
	}
	private static int normalise(int value, int min, int max) {
		int difference = max-min+1;
		if (value > min ) {
			while (value > max) {
				value = value - difference;
			}
		} else if (value < min) {
			while (value < min) {
				value = value + difference;
			}
		}
		return value;
	}
	private static int getIndex(String value, String[] array) {
		for(int i=0; i<array.length; i++) {
			if (array[i].compareTo(value) == 0) {
				return i;
			}
		}
		return -1;
	}
	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	protected static boolean isCharacter(char ch) {
		String value = "" + ch;
		return ("A".compareTo(""+value) <= 0 && "Z".compareTo(value) >= 0) || ("a".compareTo(value) <= 0 && "z".compareTo(value) >= 0);
	}
	protected static boolean isNoteCharacter(char ch) {
		String value = "" + ch;
		return ("A".compareTo(""+value) <= 0 && "G".compareTo(value) >= 0) || ("a".compareTo(value) <= 0 && "g".compareTo(value) >= 0);
	}
	public static boolean isFlat(String element) {
		return isInProperty(element,"notetation.flat");
	}
	public static boolean isSharp(String element) {
		return isInProperty(element,"notetation.sharp");
	}
	public static boolean isSharpNote(String note) {
		if (note.length() > 2) {
			return isSharp(note.substring(note.length()-2, note.length()-1));
		}
		return false;
	}
	public static boolean isFlatNote(String note) {
		if (note.length() > 2) {
			return isFlat(note.substring(note.length()-2, note.length()-1));
		}
		return false;
	}
	public static boolean isSharpKey(String keystr) {
		return isSharpKey(new Note(keystr));
	}
	public static boolean isSharpKey(Note key) {
		return isSharpNote(key.getCreation()) || isSharpCycle(key.getIndex());
	}
	
	public static boolean isSharpCycle(int index) {
		return getCycleIndex(index) <= 5;
	}
	public static int getCycleIndex(int index) {
		return NOTES_CYCLE_MAP[normaliseInterval(index)];
	}
	
	//
	// Property read file functions
	//
	private static boolean isInProperty(String value, String property) {
		String[] properties = readPropertyArray(property);
		for(int i=0; i<properties.length; i++) {
			if(value.compareTo(properties[i]) == 0) {
				return true;
			}
		}
		return false;
	}
	protected static String readProperty(String key) {
		return scales.getString(key);
	}
	protected static String[] readPropertyArray(String key) {
		return readProperty(key).split(",");
	}
	protected static String[] readPropertyScale(String scalename) {
		return readPropertyArray("scale."+scalename);
	}
	protected static String[] readStringIntervals(String scalename) {
		String[] result = readPropertyScale(scalename);
		if (result.length == 1) {
			result = result[0].split(":");
			if (result.length == 1) {
				return readStringIntervals(result[0]);
			} else {
				return getMode(readStringIntervals(result[0]),Integer.parseInt(result[1]));
			}
		} else {
			return result;
		}
	}
	protected static int[] readIntIntervals(String scalename) {
		return getIntervals(readStringIntervals(scalename));
	}
	protected static ScaleIntervals readIntervals(String scalename) {
		return new ScaleIntervals(readIntIntervals(scalename));
	}
}