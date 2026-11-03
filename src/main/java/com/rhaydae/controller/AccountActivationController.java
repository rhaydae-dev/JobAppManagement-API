
package com.rhaydae.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rhaydae.dto.ActivateAccountRequest;
import com.rhaydae.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccountActivationController {

    private final UserService userService;

    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(
            @Valid @RequestBody ActivateAccountRequest request) {

        userService.activateAccount(request);

        return ResponseEntity.ok(
                "Account activated successfully. You can now log in."
        );
    }
}
