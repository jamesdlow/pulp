package com.jameslow.pulp;

public interface Transposeable {
	public Object transpose(Note currentkey, Note newkey);
	public Object transpose(Note currentkey, Note newkey, boolean quick);
	//public Object transpose(Note currentkey, String newkeystr);
	//public Object transpose(String currentkeystr, Note newkey);
	public Object transpose(String currentkeystr, String newkeystr);
	public Object transpose(String currentkeystr, String newkeystr, boolean quick);
}
