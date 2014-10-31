package com.jameslow.pulp;

public class ScaleIntervals {
	private int[] intervals;
	
	public ScaleIntervals(String scalename) {
		this(Theory.readStringIntervals(scalename));
	}
	public ScaleIntervals(String[] intervals) {
		this(Theory.getIntervals(intervals));
	}
	public ScaleIntervals(int[] intervals) {
		this.intervals = intervals;
	}
	public int[] getIntIntervals() {
		return intervals;
	}
	public String[] getStringIntervals() {
		return Theory.getIntervals(intervals);
	}
}
