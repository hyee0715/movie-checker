package com.prgrms.moviechecker.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TelegramProperty {

    @Value("${telegram.username}")
    private String username;

    @Value("${telegram.token}")
    private String token;
}
