
package com.rhaydae.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhaydae.dto.ApplicationRequest;
import com.rhaydae.dto.ApplicationResponse;
import com.rhaydae.dto.ApplicationStatusRequest;
import com.rhaydae.service.ApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    @Operation(security = {
        @SecurityRequirement(name = "bearerAuth")
    })
    public ApplicationResponse apply(@Valid @RequestBody ApplicationRequest request,
            org.springframework.security.core.Authentication authentication) {

        return applicationService.apply(
            request, authentication.getName());
    }

    @GetMapping("/myApplications")
    @Operation(security = {
        @SecurityRequirement(name = "bearerAuth")
    })
    public List<ApplicationResponse> getMyApplications(
            org.springframework.security.core.Authentication authentication) {

        return applicationService.getMyApplications(
            authentication.getName());
    }

    @GetMapping("/job/{jobOfferId}")
    @Operation(security = {
        @SecurityRequirement(name = "bearerAuth")
    })
    public List<ApplicationResponse> getApplicationsForJob(
            @PathVariable Long jobOfferId,
            org.springframework.security.core.Authentication authentication) {

        return applicationService.getApplicationsForJob(
            jobOfferId, authentication.getName());
    }

    @PatchMapping("/updateStatus/{applicationId}")
    @Operation(security = {
        @SecurityRequirement(name = "bearerAuth")
    })
    public ApplicationResponse updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusRequest request,
            org.springframework.security.core.Authentication authentication) {

        return applicationService.updateStatus(
            applicationId,
            request.status(),
            authentication.getName());
    }
}
