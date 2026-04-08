package org.example.footballmanager.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.config.JwtProperties;
import org.example.footballmanager.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {
        AuthTokens tokens = authService.login(request);

        Cookie cookie = new Cookie("refreshToken", tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true HTTPS for production
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge((int)jwtProperties.getRefreshExpiration());
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponseDto(tokens.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);


        if (refreshToken == null || !jwtService.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        String newAccessToken = jwtService.generateAccessTokenFromRefresh(refreshToken);
        return ResponseEntity.ok(new JwtResponseDto(newAccessToken));
    }
}