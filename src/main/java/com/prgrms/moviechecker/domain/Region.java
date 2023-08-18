package com.prgrms.moviechecker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Region {

    SEOUL("0105001"),
    GYEONGGI("0105002"),
    GANGWON("0105003"),
    CHUNGCHEONGNORTH("0105004"),
    CHUNGCHEONGSOUTH("0105005"),
    GYEONGSANGNORTH("0105006"),
    GYEONGSANGSOUTH("0105007"),
    JEOLLABUKNORTH("0105008"),
    JEOLLABUKSOUTH("0105009"),
    JEJU("0105010"),
    BUSAN("0105011"),
    DAEGU("0105012"),
    DAEJEON("0105013"),
    ULSAN("0105014"),
    INCHEON("0105015"),
    GWANGJU("0105016"),
    SEJONG("0105017");

    final String sWideareaCd;
}
