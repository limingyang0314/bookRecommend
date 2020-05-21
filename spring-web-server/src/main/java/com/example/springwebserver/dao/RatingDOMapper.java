package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.RatingDO;
import com.example.springwebserver.dataObject.RatingDOKey;

public interface RatingDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ratings
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    int deleteByPrimaryKey(RatingDOKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ratings
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    int insert(RatingDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ratings
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    int insertSelective(RatingDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ratings
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    RatingDO selectByPrimaryKey(RatingDOKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ratings
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    int updateByPrimaryKeySelective(RatingDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ratings
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    int updateByPrimaryKey(RatingDO record);
}