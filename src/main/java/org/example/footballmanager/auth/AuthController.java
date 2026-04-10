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
        response.addCookie(createRefreshTokenCookie(tokens.getRefreshToken()));
        return ResponseEntity.ok(new JwtResponseDto(tokens.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(createDeleteRefreshCookie());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("###### refresh ######  ");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(401).build();
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);


        if (refreshToken == null || !jwtService.isRefreshTokenValid(refreshToken)) {
            if (refreshToken != null) {
                response.addCookie(createDeleteRefreshCookie());
            }

            return ResponseEntity.status(401).build();
        }

        String newAccessToken = jwtService.generateAccessTokenFromRefresh(refreshToken);
        return ResponseEntity.ok(new JwtResponseDto(newAccessToken));
    }

    private Cookie createDeleteRefreshCookie() {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true HTTPS for production
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Lax"); //  None + Secure=true
        return cookie;
    }

    public Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true HTTPS for production
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge((int)jwtProperties.getRefreshExpiration());
        cookie.setAttribute("SameSite", "Lax"); //  None + Secure=true
        return cookie;
    }
}