package com.example.springwebserver.dataObject;

import java.util.Date;

public class PurchaseHistoryDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_history.order_id
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    private Long orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_history.user_id
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_history.book_ids
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    private String bookIds;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_history.quantity
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    private String quantity;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_history.create_time
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_history.status
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column purchase_history.finish_time
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    private Date finishTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_history.order_id
     *
     * @return the value of purchase_history.order_id
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_history.order_id
     *
     * @param orderId the value for purchase_history.order_id
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_history.user_id
     *
     * @return the value of purchase_history.user_id
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_history.user_id
     *
     * @param userId the value for purchase_history.user_id
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_history.book_ids
     *
     * @return the value of purchase_history.book_ids
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public String getBookIds() {
        return bookIds;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_history.book_ids
     *
     * @param bookIds the value for purchase_history.book_ids
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public void setBookIds(String bookIds) {
        this.bookIds = bookIds == null ? null : bookIds.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_history.quantity
     *
     * @return the value of purchase_history.quantity
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_history.quantity
     *
     * @param quantity the value for purchase_history.quantity
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? null : quantity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_history.create_time
     *
     * @return the value of purchase_history.create_time
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_history.create_time
     *
     * @param createTime the value for purchase_history.create_time
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_history.status
     *
     * @return the value of purchase_history.status
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_history.status
     *
     * @param status the value for purchase_history.status
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column purchase_history.finish_time
     *
     * @return the value of purchase_history.finish_time
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column purchase_history.finish_time
     *
     * @param finishTime the value for purchase_history.finish_time
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}