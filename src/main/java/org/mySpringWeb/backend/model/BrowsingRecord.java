package org.mySpringWeb.backend.model;

/**
 * 访问记录模型
 * @author limingyang
 *
 */
public class BrowsingRecord {
	private String browsingID;
	private String userID;
	private String bookID;
	private String time;
	
	public String getBrowsingID() {
		return browsingID;
	}
	
	public void setBrowsingID(String browsingID) {
		this.browsingID = browsingID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getBookID() {
		return bookID;
	}
	
}
