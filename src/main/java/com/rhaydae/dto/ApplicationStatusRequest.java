
package com.rhaydae.dto;

import com.rhaydae.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusRequest(
		
    @NotNull(message = "Status is required")
    ApplicationStatus status
) {}
