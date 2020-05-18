package org.mySpringWeb.backend.model;

public class Book {
	private String bookID;
	private String ISBN;
	private String bookName;
	private String coverUrl;
	private String authorID;
	private String countryID;
	private String publisher;
	private String introduction;
	private String labelIDs;
	
	public String getISBN() {
		return ISBN;
	}
	
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	
	public String getBookName() {
		return bookName;
	}
	
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	public String getCoverUrl() {
		return coverUrl;
	}
	
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	
	public String getAuthorID() {
		return authorID;
	}
	
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	
	public String getCountryID() {
		return countryID;
	}
	
	public void setCountryID(String countryID) {
		this.countryID = countryID;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getIntroduction() {
		return introduction;
	}
	
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public String getLabelIDs() {
		return labelIDs;
	}
	
	public void setLabelIDs(String labelIDs) {
		this.labelIDs = labelIDs;
	}
	
	public String getBookID() {
		return bookID;
	}
	
}
