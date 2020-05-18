package org.mySpringWeb.backend.model;

/**
 * 书评模型
 * @author limingyang
 *
 */
public class Review {
	private String reviewID;
	private String bookID;
	private String content;
	private String reviewTime;
	private String buyTime;
	private int agreeNumber;
	private int star;
	
	public String getBookID() {
		return bookID;
	}
	
	public void setBookID(String bookID) {
		this.bookID = bookID;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getStar() {
		return star;
	}
	
	public void setStar(int star) {
		this.star = star;
	}
	
	public String getBuyTime() {
		return buyTime;
	}
	
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	
	public int getAgreeNumber() {
		return agreeNumber;
	}
	
	public void setAgreeNumber(int agreeNumber) {
		this.agreeNumber = agreeNumber;
	}
	
	public String getReviewTime() {
		return reviewTime;
	}
	
	public void setReviewTime(String reviewTime) {
		this.reviewTime = reviewTime;
	}
	
	public String getReviewID() {
		return reviewID;
	}
}
