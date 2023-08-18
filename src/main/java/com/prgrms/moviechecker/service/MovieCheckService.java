package com.prgrms.moviechecker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.moviechecker.domain.AreaInformation;
import com.prgrms.moviechecker.domain.BasareaCdListDto;
import com.prgrms.moviechecker.domain.TheaCdListDto;
import com.prgrms.moviechecker.feign.MovieCheckFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MovieCheckService {

    private final MovieCheckFeignClient movieCheckFeignClient;

    public List<AreaInformation> getBasareaCdList(String sWideareaCd) throws JsonProcessingException {
        String content = movieCheckFeignClient.getBasareaCdList(sWideareaCd);

        return jsonTest(content, BasareaCdListDto.class);
    }

    public List<AreaInformation> getTheaCdList(String sWideareaCd, String sBasareaCd) throws JsonProcessingException {
        String content = movieCheckFeignClient.getTheaCdList(sWideareaCd, sBasareaCd);

        return jsonTest(content, TheaCdListDto.class);
    }

    public static List<AreaInformation> jsonTest(String jsonString, Class<?> dtoClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Object dto = mapper.readValue(jsonString, dtoClass);

        if (dto instanceof BasareaCdListDto) {
            return ((BasareaCdListDto) dto).getBasareaCdList();
        } else if (dto instanceof TheaCdListDto) {
            return ((TheaCdListDto) dto).getTheaCdList();
        }

        return Collections.emptyList();
    }
}

