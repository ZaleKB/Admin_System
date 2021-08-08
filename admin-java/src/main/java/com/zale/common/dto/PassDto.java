package com.zale.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class PassDto implements Serializable {

	@NotBlank(message = "new password cannot be null")
	private String password;

	@NotBlank(message = "old password cannot be null")
	private String currentPass;
}
