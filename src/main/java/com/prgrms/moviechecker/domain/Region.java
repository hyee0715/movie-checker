package com.prgrms.moviechecker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Region {

    SEOUL("0105001", "서울시"),
    GYEONGGI("0105002", "경기도"),
    GANGWON("0105003", "강원도"),
    CHUNGCHEONGNORTH("0105004", "충청북도"),
    CHUNGCHEONGSOUTH("0105005", "충청남도"),
    GYEONGSANGNORTH("0105006", "경상북도"),
    GYEONGSANGSOUTH("0105007", "경상남도"),
    JEOLLABUKNORTH("0105008", "전라북도"),
    JEOLLABUKSOUTH("0105009", "전라남도"),
    JEJU("0105010", "제주도"),
    BUSAN("0105011", "부산시"),
    DAEGU("0105012", "대구시"),
    DAEJEON("0105013", "대전시"),
    ULSAN("0105014", "울산시"),
    INCHEON("0105015", "인천시"),
    GWANGJU("0105016", "광주시"),
    SEJONG("0105017", "세종시");

    final String sWideareaCd;
    final String name;

    public static String findsWideareaCd(String name) {
        return Arrays.stream(Region.values())
                .filter(Region -> Objects.equals(Region.name, name))
                .findAny()
                .map(x -> x.sWideareaCd)
                .orElseThrow(RuntimeException::new);
    }

    public static List<String> getNames() {
        return Arrays.stream(Region.values())
                .map(x -> x.name)
                .collect(Collectors.toList());
    }

    public static boolean isRegion(String sWidereaCd) {
        return Arrays.stream(Region.values())
                .anyMatch(Region -> Objects.equals(Region.sWideareaCd, sWidereaCd));
    }
}
