package matal.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import matal.jwt.repository.BlackListRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final String secretKey;
    private final BlackListRepository blackListRepository;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/register") || path.startsWith("/api/auth/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("authorization : " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.error("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        logger.info("token : " + token);

        // 블랙리스트에 토큰이 있는지 확인
        if (blackListRepository.existsByInvalidRefreshToken(token)) {
            logger.debug("토큰이 블랙리스트에 등록되어 있습니다.");
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return;
        }

        if (JwtUtil.isExpired(token, secretKey)) {
            logger.error("Token이 만료 되었습니다.");
            SecurityContextHolder.clearContext();  // 인증 정보 제거
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "로그인 시간이 만료되었습니다.");
            return;
        }

        Long id = JwtUtil.getId(token, secretKey);
        logger.info("user_id: " + id);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(id, null, List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
