package com.example.springwebserver.dataObject;

public class CountryDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column countries.country_id
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    private Integer countryId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column countries.country_name
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    private String countryName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column countries.country_id
     *
     * @return the value of countries.country_id
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column countries.country_id
     *
     * @param countryId the value for countries.country_id
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column countries.country_name
     *
     * @return the value of countries.country_name
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column countries.country_name
     *
     * @param countryName the value for countries.country_name
     *
     * @mbg.generated Thu May 21 16:27:45 CST 2020
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName == null ? null : countryName.trim();
    }
}