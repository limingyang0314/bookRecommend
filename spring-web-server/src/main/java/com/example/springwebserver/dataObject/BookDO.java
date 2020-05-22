package com.example.springwebserver.dataObject;

public class BookDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.book_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private Long bookId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.isbn
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String isbn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.book_name
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String bookName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.book_subname
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String bookSubname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.author_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private Long authorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.country_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private Integer countryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.publisher
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String publisher;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.published_place
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String publishedPlace;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.published_time
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String publishedTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.tag_ids
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String tagIds;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.page
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private Integer page;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.price
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private Double price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.sellerlist
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String sellerlist;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.cover_url
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String coverUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.rating
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private Double rating;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.rating_person_num
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private Long ratingPersonNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column books.description
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.book_id
     *
     * @return the value of books.book_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.book_id
     *
     * @param bookId the value for books.book_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.isbn
     *
     * @return the value of books.isbn
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.isbn
     *
     * @param isbn the value for books.isbn
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.book_name
     *
     * @return the value of books.book_name
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.book_name
     *
     * @param bookName the value for books.book_name
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.book_subname
     *
     * @return the value of books.book_subname
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getBookSubname() {
        return bookSubname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.book_subname
     *
     * @param bookSubname the value for books.book_subname
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setBookSubname(String bookSubname) {
        this.bookSubname = bookSubname == null ? null : bookSubname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.author_id
     *
     * @return the value of books.author_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public Long getAuthorId() {
        return authorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.author_id
     *
     * @param authorId the value for books.author_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.country_id
     *
     * @return the value of books.country_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.country_id
     *
     * @param countryId the value for books.country_id
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.publisher
     *
     * @return the value of books.publisher
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.publisher
     *
     * @param publisher the value for books.publisher
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.published_place
     *
     * @return the value of books.published_place
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getPublishedPlace() {
        return publishedPlace;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.published_place
     *
     * @param publishedPlace the value for books.published_place
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setPublishedPlace(String publishedPlace) {
        this.publishedPlace = publishedPlace == null ? null : publishedPlace.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.published_time
     *
     * @return the value of books.published_time
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getPublishedTime() {
        return publishedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.published_time
     *
     * @param publishedTime the value for books.published_time
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime == null ? null : publishedTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.tag_ids
     *
     * @return the value of books.tag_ids
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getTagIds() {
        return tagIds;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.tag_ids
     *
     * @param tagIds the value for books.tag_ids
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setTagIds(String tagIds) {
        this.tagIds = tagIds == null ? null : tagIds.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.page
     *
     * @return the value of books.page
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public Integer getPage() {
        return page;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.page
     *
     * @param page the value for books.page
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.price
     *
     * @return the value of books.price
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public Double getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.price
     *
     * @param price the value for books.price
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.sellerlist
     *
     * @return the value of books.sellerlist
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getSellerlist() {
        return sellerlist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.sellerlist
     *
     * @param sellerlist the value for books.sellerlist
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setSellerlist(String sellerlist) {
        this.sellerlist = sellerlist == null ? null : sellerlist.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.cover_url
     *
     * @return the value of books.cover_url
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getCoverUrl() {
        return coverUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.cover_url
     *
     * @param coverUrl the value for books.cover_url
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl == null ? null : coverUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.rating
     *
     * @return the value of books.rating
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public Double getRating() {
        return rating;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.rating
     *
     * @param rating the value for books.rating
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.rating_person_num
     *
     * @return the value of books.rating_person_num
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public Long getRatingPersonNum() {
        return ratingPersonNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.rating_person_num
     *
     * @param ratingPersonNum the value for books.rating_person_num
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setRatingPersonNum(Long ratingPersonNum) {
        this.ratingPersonNum = ratingPersonNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column books.description
     *
     * @return the value of books.description
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column books.description
     *
     * @param description the value for books.description
     *
     * @mbg.generated Fri May 22 18:08:15 CST 2020
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}