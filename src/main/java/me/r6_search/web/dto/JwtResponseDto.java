package me.r6_search.web.dto;

import lombok.Getter;

@Getter
public class JwtResponseDto {
    private final String jwtToken;

    public JwtResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}