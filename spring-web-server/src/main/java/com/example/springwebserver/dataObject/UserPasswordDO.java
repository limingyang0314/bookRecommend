package com.example.springwebserver.dataObject;

public class UserPasswordDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users_password.user_id
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column users_password.password
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    private String password;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users_password.user_id
     *
     * @return the value of users_password.user_id
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users_password.user_id
     *
     * @param userId the value for users_password.user_id
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column users_password.password
     *
     * @return the value of users_password.password
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column users_password.password
     *
     * @param password the value for users_password.password
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}