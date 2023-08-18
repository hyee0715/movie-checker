package com.prgrms.moviechecker.service;

import com.prgrms.moviechecker.domain.TelegramProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Service
@Slf4j
public class TelegramService extends TelegramLongPollingBot {

    private final TelegramProperty telegramProperty;

    @Override
    public String getBotUsername() {
        return telegramProperty.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        log.info(message.toString());
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText("자동 응답으로 보낼 메시지");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotToken() {
        return telegramProperty.getToken();
    }
}