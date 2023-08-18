package com.prgrms.moviechecker.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleDto {
    List<HomepgUrl> theater;
    List<Schedule> schedule;
}
