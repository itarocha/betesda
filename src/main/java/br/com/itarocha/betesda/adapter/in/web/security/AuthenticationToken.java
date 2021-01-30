package br.com.itarocha.betesda.adapter.in.web.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationToken {
    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";
}