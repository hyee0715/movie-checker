package com.prgrms.moviechecker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prgrms.moviechecker.domain.AreaInformation;
import com.prgrms.moviechecker.domain.Schedule;
import com.prgrms.moviechecker.service.MovieCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MovieCheckController {

    private final MovieCheckService movieCheckService;

    @GetMapping("/basarea")
    public List<AreaInformation> getBasarea(@RequestParam("sWideareaCd") String sWideareaCd) throws JsonProcessingException {
        return movieCheckService.getBasareaCdList(sWideareaCd);
    }

    @GetMapping("/thea")
    public List<AreaInformation> getTheaCd(@RequestParam("sWideareaCd") String sWideareaCd, @RequestParam("sBasareaCd") String sBasareaCd) throws JsonProcessingException {
        return movieCheckService.getTheaCdList(sWideareaCd, sBasareaCd);
    }

    @GetMapping("/schedule")
    public List<Schedule> getSchedule(@RequestParam("theaCd") String theaCd, @RequestParam("showDt") String showDt) throws JsonProcessingException {
        return movieCheckService.getSchedule(theaCd, showDt);
    }
}
