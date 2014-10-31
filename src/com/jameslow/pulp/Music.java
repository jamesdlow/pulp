package com.jameslow.pulp;

import java.util.*;

/**
 * We could make this class completely recursive so all information was implement as a Section
 * and so a section can have a list of Sections. It would make toString() / transpose() common
 * might not be necessary though, might be a waste of time, and make things unintuitive.
 * @author James
 *
 */
public class Music implements Transposeable {
	protected List<SectionProperty> properties = new ArrayList<SectionProperty>();

	public void setProperty(String line) {
		String[] keyvalue = line.split(":",2);
		/*
		if (keyvalue.length > 2) {
			String value = "";
			for (int i=1; i<keyvalue.length; i++) {
				value = value + keyvalue[i];
			}
			setProperty(keyvalue[0],value);
		} else*/if (keyvalue.length == 2) {
			setProperty(keyvalue[0],keyvalue[1]);
		} else {
			setProperty(keyvalue[0],"");
		}
	}
	public void setProperty(String type, String value) {
		if (value.length() > 0) {
			int i = 0;
			while (Song.isWhitespace(""+value.charAt(i)) && i < value.length()) {
				i++;
			}
			setProperty(SectionType.parse(type),value.substring(i),type);
		} else {
			setProperty(SectionType.parse(type),"",type);
		}
	}
	public List<SectionProperty> getProperties() {
		return properties;
	}
	public void setProperty(SectionProperty property) {
		properties.add(property);
	}
	public void setProperty(SectionType type, String value) {
		setProperty(new SectionProperty(type,value));
	}
	public void setProperty(SectionType type, String value, String creation) {
		setProperty(new SectionProperty(type,value,creation));
	}
	public String getPropertyValue(SectionType type) {
		for (SectionProperty prop : properties) {
			if (prop.type == type) {
				return prop.data;
			}
		}
		return null;
	}
	public List<String> getPropertyValues(SectionType type) {
		List<String> result = new ArrayList<String>();
		for (SectionProperty prop : properties) {
			if (prop.type == type) {
				result.add(prop.data);
			}
		}
		return result;
	}
	
	public Music copy() {
		Music music = new Music();
		for (SectionProperty prop : properties) {
			music.setProperty(prop.copy());
		}
		return music;
	}

	protected Music newthis() {
		return new Music();
	}
	
	public String getFirstLine() {
		return "";
	}
	public Music transpose(String currentkeystr, String newkeystr, boolean quick) {
		return transpose(new Note(currentkeystr),new Note(newkeystr),quick);
	}
	public Music transpose(Note currentkey, Note newkey, boolean quick) {
		if (quick && currentkey.equal(newkey)) {
			return this;
		} else {
			return transpose(currentkey, newkey);
		}
	}
	public Music transpose(String currentkeystr, String newkeystr) {
		return transpose(currentkeystr,newkeystr,false);
	}
	public Music transpose(Note currentkey, Note newkey) {
		Music music = newthis();
		for (SectionProperty prop : properties) {
			if (prop.type.isTransposeable()) {
				Note sectionkey = new Note(prop.data);
				music.setProperty(prop.type,sectionkey.transpose(currentkey, newkey).getString());
			} else {
				music.setProperty(prop.copy());
			}
		}
		return music;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("");
		for (SectionProperty prop : properties) {
			sb.append(prop.toString() + "\n");
		}
		return sb.toString();
	}
}