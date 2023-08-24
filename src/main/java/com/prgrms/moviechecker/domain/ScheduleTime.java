package com.prgrms.moviechecker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScheduleTime {
    int hour;
    int minute;

    @Override
    public String toString() {
        return hour + "시 " + minute + "분";
    }
}
