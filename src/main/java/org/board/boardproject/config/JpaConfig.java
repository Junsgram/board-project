package org.board.boardproject.config;

import org.board.boardproject.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        // SecurityContextHolder -> 시큐리티의 모든 정보를 가지고 있는 class
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                // getContext메소드를 통해 SecurityContext class 를 가져올 수 있다. 여기에는 Authentication의 정보를 가지고 있다.
                .map(SecurityContext::getAuthentication)
                // Authentication이 인증이 되었는 지 mapping
                .filter(Authentication::isAuthenticated)
                // 로그인 정보인 Principal(보편적인 Principal정보)
                .map(Authentication::getPrincipal)
                // Principal 구현체로 생성된 BoardPrincipal에서 상속받은 userDetail을 통해 인증 정보를 가져온다
                .map(BoardPrincipal.class::cast)
                 // BoardPrincipal에서 생성한 username의 값을 가지고 온다
                .map(BoardPrincipal::getUsername);
    }

}
