package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.UserDO;

public interface UserDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    int insert(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    int insertSelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    UserDO selectByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Wed May 20 16:57:13 CST 2020
     */
    int updateByPrimaryKey(UserDO record);


    UserDO selectByUserName(String userName);

    UserDO selectOneUserNotRatingTheBook(Long bookId);
}