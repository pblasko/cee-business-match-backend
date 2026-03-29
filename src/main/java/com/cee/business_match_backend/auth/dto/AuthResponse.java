package com.cee.business_match_backend.auth.dto;

import com.cee.business_match_backend.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long userId;
    private String email;
    private Role role;
    private String token;
    private String message;

}
