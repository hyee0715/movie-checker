package com.prgrms.moviechecker.config;

import com.prgrms.moviechecker.domain.TelegramProperty;
import com.prgrms.moviechecker.service.MovieCheckService;
import com.prgrms.moviechecker.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@Configuration
public class TelegramConfig {

    private final TelegramProperty telegramProperty;
    private final MovieCheckService movieCheckService;

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new TelegramService(movieCheckService, telegramProperty));
        return api;
    }
}
