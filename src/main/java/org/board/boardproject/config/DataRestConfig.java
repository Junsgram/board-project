package org.board.boardproject.config;

import org.board.boardproject.domain.UserAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class DataRestConfig {
    // RepositoryRestConfigurer 빈 생성
    @Bean
    // userId가 노출이 될 수 있도록 지정
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig((config, cors) ->
                config.exposeIdsFor(UserAccount.class)
        );
    }

}
