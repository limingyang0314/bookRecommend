package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.ReviewDO;

import java.util.List;

public interface ReviewDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reviews
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    int deleteByPrimaryKey(Long reviewId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reviews
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    int insert(ReviewDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reviews
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    int insertSelective(ReviewDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reviews
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    ReviewDO selectByPrimaryKey(Long reviewId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reviews
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    int updateByPrimaryKeySelective(ReviewDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reviews
     *
     * @mbg.generated Thu May 21 21:11:28 CST 2020
     */
    int updateByPrimaryKey(ReviewDO record);

    List<ReviewDO> listReviewByUserID(Long userID);
}