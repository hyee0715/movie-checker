package com.prgrms.moviechecker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.moviechecker.domain.*;
import com.prgrms.moviechecker.domain.dto.BasareaCdListDto;
import com.prgrms.moviechecker.domain.dto.ScheduleDto;
import com.prgrms.moviechecker.domain.dto.TheaCdListDto;
import com.prgrms.moviechecker.feign.MovieCheckFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MovieCheckService {

    private final MovieCheckFeignClient movieCheckFeignClient;

    public List<AreaInformation> getBasareaCdList(String sWideareaCd) throws JsonProcessingException {
        String content = movieCheckFeignClient.getBasareaCdList(sWideareaCd);

        return convertAreaInformationJsonData(content, BasareaCdListDto.class);
    }

    public List<AreaInformation> getTheaCdList(String sWideareaCd, String sBasareaCd) throws JsonProcessingException {
        String content = movieCheckFeignClient.getTheaCdList(sWideareaCd, sBasareaCd);

        return convertAreaInformationJsonData(content, TheaCdListDto.class);
    }

    public List<Schedule> getSchedule(String theaCd, String showDt) throws JsonProcessingException {
        String content = movieCheckFeignClient.getSchedule(theaCd, showDt);

        List<Schedule> schedules = convertScheduleJsonData(content);

        for (Schedule schedule : schedules) {
            String showTm = schedule.getShowTm();
            List<ScheduleTime> scheduleTimes = extractScheduleTime(showTm);
            schedule.setScheduleTimes(scheduleTimes);
        }

        return schedules;
    }

    public List<ScheduleTime> extractScheduleTime(String showTm) {
        List<ScheduleTime> ret = new ArrayList<>();
        String[] showtimes = showTm.split(",");

        for (String showtime : showtimes) {
            String hour = showtime.substring(0, 2);
            String minute = showtime.substring(2, 4);

            ScheduleTime scheduleTime = new ScheduleTime(Integer.parseInt(hour), Integer.parseInt(minute));
            ret.add(scheduleTime);
        }

        return ret;
    }

    public static List<AreaInformation> convertAreaInformationJsonData(String jsonString, Class<?> dtoClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Object dto = mapper.readValue(jsonString, dtoClass);

        if (dto instanceof BasareaCdListDto) {
            return ((BasareaCdListDto) dto).getBasareaCdList();
        } else if (dto instanceof TheaCdListDto) {
            return ((TheaCdListDto) dto).getTheaCdList();
        }

        return Collections.emptyList();
    }

    public static List<Schedule> convertScheduleJsonData(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ScheduleDto scheduleDto = mapper.readValue(jsonString, ScheduleDto.class);

        return scheduleDto.getSchedule();
    }

    public List<Schedule> getCorrectTimeSchedules(List<Schedule> schedules, String time) {
        List<Schedule> ret = new ArrayList<>();

        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(2, 4));

        int[] wantTime = {hour, minute};

        for (Schedule schedule : schedules) {
            List<ScheduleTime> scheduleTimes = schedule.getScheduleTimes();

            boolean selectSchedule = false;

            for (ScheduleTime scheduleTime : scheduleTimes) {
                if (scheduleTime.getHour() == wantTime[0] || scheduleTime.getHour() - 1 == wantTime[0] || scheduleTime.getHour() + 1 == wantTime[0]) {
                    selectSchedule = true;
                    break;
                }
            }

            if (selectSchedule) {
                ret.add(schedule);
            }
        }

        return ret;
    }
}

