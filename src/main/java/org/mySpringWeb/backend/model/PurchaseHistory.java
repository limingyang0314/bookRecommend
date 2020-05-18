package org.mySpringWeb.backend.model;

/**
 * 购买记录模型
 * @author limingyang
 *
 */
public class PurchaseHistory {
	private String orderID;
	private String userID;
	private String bookIDs;
	private String quantity;
	private String createTime;
	private int status;
	private String finishTime;
	
	public String getOrderID() {
		return orderID;
	}
	
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getQuantity() {
		return quantity;
	}
	
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getFinishTime() {
		return finishTime;
	}
	
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	
	public String getBookIDs() {
		return bookIDs;
	}
	
	public String getCreateTime() {
		return createTime;
	}

}
