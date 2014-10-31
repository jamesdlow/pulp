package com.jameslow.pulp;

public class SectionProperty {
	public SectionType type;
	public String creation;
	public String data;
	
	public SectionProperty(final SectionType type, String data) {
		this(type,data,type.getDefault());
	}
	public SectionProperty(final SectionType type, String data, String creation) {
		this.type = type;
		this.creation = creation;
		this.data = data;
	}
	public SectionProperty copy() {
		return new SectionProperty(type,data,creation);
	}
	public String getTitle() {
		return (type == SectionType.UNKNOWN ? creation : type.getDefault());
	}
	public String toString() {
		return getTitle() + ": " + (data == null ? "" : data);
	}
}
