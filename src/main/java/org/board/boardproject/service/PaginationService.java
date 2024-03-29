package org.board.boardproject.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    // 뷰에서 보여지는 페이지 넘버링의 숫자를 상수로 만들어서 사용
    private static final int BAR_LENGTH = 5;
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        // Math.max(int1, int2) int의 최대값을 호출하는 메소드로 해당 값에 0을 넣어 음수값이 담기지 않도록 구현
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2),0);

        // Math.min() 주어진 값에서 가장 작은 값을 사용하는 메소드
        int endNumber = Math.min(startNumber+BAR_LENGTH, totalPages);

        // 배열로 리턴 받은 값을 boxing해주고 list로 처리
        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
