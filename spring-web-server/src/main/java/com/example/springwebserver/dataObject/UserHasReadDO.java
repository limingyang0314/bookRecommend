package com.example.springwebserver.dataObject;

public class UserHasReadDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users_has_read.user_ID
     *
     * @mbg.generated Sun May 24 23:54:15 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users_has_read.books
     *
     * @mbg.generated Sun May 24 23:54:15 CST 2020
     */
    private String books;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users_has_read.user_ID
     *
     * @return the value of users_has_read.user_ID
     *
     * @mbg.generated Sun May 24 23:54:15 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users_has_read.user_ID
     *
     * @param userId the value for users_has_read.user_ID
     *
     * @mbg.generated Sun May 24 23:54:15 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users_has_read.books
     *
     * @return the value of users_has_read.books
     *
     * @mbg.generated Sun May 24 23:54:15 CST 2020
     */
    public String getBooks() {
        return books;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users_has_read.books
     *
     * @param books the value for users_has_read.books
     *
     * @mbg.generated Sun May 24 23:54:15 CST 2020
     */
    public void setBooks(String books) {
        this.books = books == null ? null : books.trim();
    }
}