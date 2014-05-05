package com.capgemini.log;

public class DeepId implements Comparable<DeepId> {
	private String id;
	private String thread;
	public DeepId(String thread, Long layoutId) {
		this.thread = thread;
		this.id = thread + (layoutId != null ? "-" + layoutId : "");
	}
	
	public boolean equals(Object object) {
		return object instanceof DeepId && compareTo((DeepId) object) == 0;
	}

	public int compareTo(DeepId deepId) {
		return id.compareTo(deepId.id);
	}
	
	public String toString() {
		return id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getThread() {
		return thread;
	}
}

