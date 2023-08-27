package com.prgrms.moviechecker.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Schedule {
    String scrnNm;
    String movieNm;
    String movieCd;
    String showTm;
    List<ScheduleTime> scheduleTimes;

    @Override
    public String toString() {
        return "\n\n [상영관 번호 = " + scrnNm +
                "\n 영화 제목 = " + movieNm +
                "\n 상영 시작 시간 = " + scheduleTimes;
    }
}
