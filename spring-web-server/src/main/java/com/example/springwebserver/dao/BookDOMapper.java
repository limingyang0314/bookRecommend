package com.example.springwebserver.dao;

import com.example.springwebserver.dataObject.BookDO;
import com.github.pagehelper.Page;

import java.util.List;

public interface BookDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    int deleteByPrimaryKey(Long bookId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    int insert(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    int insertSelective(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    BookDO selectByPrimaryKey(Long bookId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    int updateByPrimaryKeySelective(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    int updateByPrimaryKeyWithBLOBs(BookDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table books
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    int updateByPrimaryKey(BookDO record);

    Page<BookDO> listBookByAuthorPage(String authorID);

    Page<BookDO> listBookByTagPage(String tagID);

    Page<BookDO> listBookByBookIDSetPage(List<String> books);

    List<BookDO> listBookByBookIDSet(List<String> books);

    Page<BookDO> listBookByHotRank();

    Page<BookDO> listBookByPage();

    Page<BookDO> listBookBySearchKey(String key);

    List<BookDO> selectByPrimaryKeys(List<Long> bookIds);

}