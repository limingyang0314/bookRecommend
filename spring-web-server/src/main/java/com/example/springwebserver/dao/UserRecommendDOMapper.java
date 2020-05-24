package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.UserRecommendDO;

public interface UserRecommendDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users_recommend
     *
     * @mbg.generated Sun May 24 20:14:54 CST 2020
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users_recommend
     *
     * @mbg.generated Sun May 24 20:14:54 CST 2020
     */
    int insert(UserRecommendDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users_recommend
     *
     * @mbg.generated Sun May 24 20:14:54 CST 2020
     */
    int insertSelective(UserRecommendDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users_recommend
     *
     * @mbg.generated Sun May 24 20:14:54 CST 2020
     */
    UserRecommendDO selectByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users_recommend
     *
     * @mbg.generated Sun May 24 20:14:54 CST 2020
     */
    int updateByPrimaryKeySelective(UserRecommendDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users_recommend
     *
     * @mbg.generated Sun May 24 20:14:54 CST 2020
     */
    int updateByPrimaryKey(UserRecommendDO record);
}