package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.AdminDO;

public interface AdminDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admins
     *
     * @mbg.generated Mon Jun 08 11:04:35 CST 2020
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admins
     *
     * @mbg.generated Mon Jun 08 11:04:35 CST 2020
     */
    int insert(AdminDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admins
     *
     * @mbg.generated Mon Jun 08 11:04:35 CST 2020
     */
    int insertSelective(AdminDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admins
     *
     * @mbg.generated Mon Jun 08 11:04:35 CST 2020
     */
    AdminDO selectByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admins
     *
     * @mbg.generated Mon Jun 08 11:04:35 CST 2020
     */
    int updateByPrimaryKeySelective(AdminDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admins
     *
     * @mbg.generated Mon Jun 08 11:04:35 CST 2020
     */
    int updateByPrimaryKey(AdminDO record);
}