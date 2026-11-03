
package com.rhaydae.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rhaydae.dto.NotificationResponse;
import com.rhaydae.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/myNotifications")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public List<NotificationResponse> getMyNotifications(
            Authentication authentication) {

        return notificationService.getMyNotifications(
                authentication.getName());
    }

    @PatchMapping("/markAsRead/{notificationId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public void markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication) {

        notificationService.markAsRead(
                notificationId,
                authentication.getName());
    }
}

