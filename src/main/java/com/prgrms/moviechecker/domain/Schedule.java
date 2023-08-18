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
}
