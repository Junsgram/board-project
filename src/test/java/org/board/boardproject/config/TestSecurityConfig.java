package org.board.boardproject.config;

import org.board.boardproject.domain.UserAccount;
import org.board.boardproject.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;


    @BeforeTestMethod // spring test할때 즉, 인증 테스트를 진행할 때에만 사용 - 테스트 메소드를 실행되기 직전에 사용하는 메소드
    public void securitySetup() {
        /**
         * 테스트를 진행하기 전 인증 정보를 넣어줘야 하는데
         * Security Config에서 userDetailsService로 Repository를 통해 값을 찾아 해당 테스트를 구현
         */
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
                            "junTest",
                            "pw",
                            "Jun-test@mail.com",
                            "jun-test",
                            "test memo"
                )));
    }
}
