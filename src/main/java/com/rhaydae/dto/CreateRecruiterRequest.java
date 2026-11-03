package com.rhaydae.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRecruiterRequest(

		 @NotBlank(message = "First Name is required")
		    @Size(max = 50)
		    String firstName,

		    @NotBlank(message = "Last Name is required")
		    @Size(max = 50)
		    String lastName,

		    @NotBlank(message = "Email is required")
		    @Email(message = "Invalid email format")
		    String email
		    
) {
}
