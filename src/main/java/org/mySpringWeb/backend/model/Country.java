package org.mySpringWeb.backend.model;

/**
 * 国家地区模型
 * @author limingyang
 *
 */
public class Country {
	private String countryID;
	private String countryName;
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getCountryID() {
		return countryID;
	}
}
