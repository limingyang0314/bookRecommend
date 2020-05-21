package com.example.springwebserver.service.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserModel implements Serializable {

    private Long userId;

    @NotBlank(message = "user name can't be blank")
    private String userName;

    @NotNull(message = "age can't be null")
    @Min(value = 0, message = "age can't less than 0")
    @Max(value = 150, message = "age can't large than 150")
    private Integer age;

    @NotNull(message = "gender can't be null")
    private Boolean gender;

    private String introduction;

    @NotBlank(message = "password can't be null")
    private String encryptPassword;

	public void setEncryptPassword(String password) {
		// TODO Auto-generated method stub
		
	}

	public void setUserId(Long userId2) {
		// TODO Auto-generated method stub
		
	}

	public String getEncryptPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getUserId() {
		// TODO Auto-generated method stub
		return null;
	}
}
