package org.mySpringWeb.backend.model;

/**
 * 作者模型
 * @author limingyang
 *
 */
public class Author {
	private String authorID;
	private String authorName;
	private Integer countryID;
	
	public String getAuthorName() {
		return authorName;
	}
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	public Integer getCountryID() {
		return countryID;
	}
	
	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}
	
	public String getAuthorID() {
		return authorID;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorID == null) ? 0 : authorID.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		if (authorID == null) {
			if (other.authorID != null)
				return false;
		} else if (!authorID.equals(other.authorID))
			return false;
		return true;
	}
	
}
