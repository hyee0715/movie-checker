package com.prgrms.moviechecker.domain.dto;

import com.prgrms.moviechecker.domain.HomepgUrl;
import com.prgrms.moviechecker.domain.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleDto {
    List<HomepgUrl> theater;
    List<Schedule> schedule;
}
