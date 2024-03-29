package org.board.boardproject.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

// assertJ 테스트
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이징 구현")
// 테스트의 성능을 높이기 위해서
/**
 * 스프링부트의 테스트는 여러가지 옵션 중 대표적인 webEnvironment을 주고
 * 기본 값은 mock이지만, none을 넣어 webEnvironment의 무게를 줄이고
 * 통합테스트의 경로를 임의로 지정하여 스프링부트의 테스트의 무게는 줄어 테스트의 성능은 높아진다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
public class PaginationServiceTest {
    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }

    @DisplayName("현재 페이지의 번호와 총 페이지의 번호를 받아 페이징 바를 구현")
    @MethodSource
    @ParameterizedTest(name="[{index}] 현재페이: {0}, 총 페이지: {1} => {2}")
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers(int currentPageNumber,
                                                                                             int totalPages,
                                                                                             List<Integer> expected) {
        // Given

        // When
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber, totalPages);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers() {
        return Stream.of(
            arguments(0,13,List.of(0,1,2,3,4)),
            arguments(1,13,List.of(0,1,2,3,4)),
            arguments(2,13,List.of(0,1,2,3,4)),
            arguments(3,13,List.of(1,2,3,4,5)),
            arguments(4,13,List.of(2,3,4,5,6)),
            arguments(5,13,List.of(3,4,5,6,7)),
            arguments(6,13,List.of(4,5,6,7,8)),
            arguments(10,13,List.of(8,9,10,11,12)),
            arguments(11,13,List.of(9,10,11,12)),
            arguments(12,13,List.of(10,11,12))
        );
    }

    @DisplayName("현재 서비스에서 설정한 페이지 네이션의 길이를 확인하기 위한 테스트")
    @Test
    void givenNothing_whenCalling_thenReturnsCurrentBarLength() {
        // Given

        // When
        int barLength = sut.currentBarLength();
        // Then
        assertThat(barLength).isEqualTo(5);
    }
}
