package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.AuthorDO;

public interface AuthorDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table authors
     *
     * @mbg.generated Thu May 21 14:48:38 CST 2020
     */
    int deleteByPrimaryKey(Long authorId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table authors
     *
     * @mbg.generated Thu May 21 14:48:38 CST 2020
     */
    int insert(AuthorDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table authors
     *
     * @mbg.generated Thu May 21 14:48:38 CST 2020
     */
    int insertSelective(AuthorDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table authors
     *
     * @mbg.generated Thu May 21 14:48:38 CST 2020
     */
    AuthorDO selectByPrimaryKey(Long authorId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table authors
     *
     * @mbg.generated Thu May 21 14:48:38 CST 2020
     */
    int updateByPrimaryKeySelective(AuthorDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table authors
     *
     * @mbg.generated Thu May 21 14:48:38 CST 2020
     */
    int updateByPrimaryKey(AuthorDO record);
}