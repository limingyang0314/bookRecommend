package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.BookDO;
import com.example.springwebserver.service.model.BookModel;
import com.github.pagehelper.Page;


public interface BookDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Wed May 20 19:20:45 CST 2020
     */
    int deleteByPrimaryKey(Long bookId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Wed May 20 19:20:45 CST 2020
     */
    int insert(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Wed May 20 19:20:45 CST 2020
     */
    int insertSelective(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Wed May 20 19:20:45 CST 2020
     */
    BookDO selectByPrimaryKey(Long bookId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Wed May 20 19:20:45 CST 2020
     */
    int updateByPrimaryKeySelective(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Wed May 20 19:20:45 CST 2020
     */
    int updateByPrimaryKeyWithBLOBs(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Wed May 20 19:20:45 CST 2020
     */
    int updateByPrimaryKey(BookDO record);

    Page<BookDO> listBookByPage();
}