package com.example.springwebserver.dataObject;

public class TagDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tags.tag_id
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    private Integer tagId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tags.tag_name
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    private String tagName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tags.tag_hot
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    private Long tagHot;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tags.tag_id
     *
     * @return the value of tags.tag_id
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tags.tag_id
     *
     * @param tagId the value for tags.tag_id
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tags.tag_name
     *
     * @return the value of tags.tag_name
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tags.tag_name
     *
     * @param tagName the value for tags.tag_name
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tags.tag_hot
     *
     * @return the value of tags.tag_hot
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    public Long getTagHot() {
        return tagHot;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tags.tag_hot
     *
     * @param tagHot the value for tags.tag_hot
     *
     * @mbg.generated Thu May 28 22:29:58 CST 2020
     */
    public void setTagHot(Long tagHot) {
        this.tagHot = tagHot;
    }
}