package com.prgrms.moviechecker.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "findBasarea", url = "https://www.kobis.or.kr/kobis/business/mast/thea")
public interface MovieCheckFeignClient {

    @GetMapping(value = "/findBasareaCdList.do")
    String getBasareaCdList(@RequestParam("sWideareaCd") String sWideareaCd);

    @GetMapping(value = "/findTheaCdList.do")
    String getTheaCdList(@RequestParam("sWideareaCd") String sWideareaCd, @RequestParam("sBasareaCd") String sBasareaCd);

    @GetMapping(value = "/findSchedule.do")
    String getSchedule(@RequestParam("theaCd") String theaCd, @RequestParam("showDt") String showDt);
}
