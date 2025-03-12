package com.generic.typed.security.jwt.filter;

import com.generic.typed.security.jwt.exception.JwtExceptionCode;
import com.generic.typed.security.jwt.token.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        log.debug("URI: {}, Method: {}", path, method);


        // 2. 인증이 필요 없는 공개 API 경로만 정의
        String[] publicPatterns = {
                "/",                     // 메인 페이지
                "/auth/signup",          // 회원가입
                "/auth/login",           // 로그인
                "/auth/refreshToken",    // 토큰 갱신
                "/books/search",         // 책 검색 API
                "/error"                 // 에러 페이지
                // 기타 필요한 공개 API 추가
        };

        // 3. OPTIONS 요청(CORS preflight)은 항상 허용
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // 4. 공개 API 패턴과 일치하는지 확인
        for (String pattern : publicPatterns) {
            if (pathMatcher.match(pattern, path)) {
                log.debug("공개 API 경로 일치: {}", pattern);
                return true;
            }
        }

        // 5. 그 외 모든 API는 JWT 인증 필요
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = "";
        try {
            token = getToken(request);
            log.info("추출된 토큰 값:[{}]", token);
            log.info("Token length: {}", (token != null ? token.length() : "null"));
            log.info("StringUtils.hasText result: {}", StringUtils.hasText(token));
            if (StringUtils.hasText(token)) {
                log.info("토큰 유효성 통과, 인증을 위한 과정 진행중");
                getAuthentication(token);
            } else {
                log.info("토큰 유효성 실패");
            }
            filterChain.doFilter(request, response);
        } catch (NullPointerException | IllegalStateException e) {
            request.setAttribute("exception", JwtExceptionCode.NOT_FOUND_TOKEN.getCode());
            log.error("Not found Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new not found token exception");
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
            log.error("Invalid Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new invalid token exception");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
            log.error("EXPIRED Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new expired token exception");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
            log.error("Unsupported Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new unsupported token exception");
        } catch (Exception e) {
            log.error("====================================================");
            log.error("JwtFilter - doFilterInternal() 오류 발생");
            log.error("token : {}", token);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("====================================================");
            throw new BadCredentialsException("throw new exception");
        }
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            String[] arr = authorization.split(" ");
            return arr[1];
        }
        return null;
    }

    private void getAuthentication(String token) {
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }


}
