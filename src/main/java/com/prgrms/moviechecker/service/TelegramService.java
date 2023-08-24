package com.prgrms.moviechecker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prgrms.moviechecker.domain.AreaInformation;
import com.prgrms.moviechecker.domain.Region;
import com.prgrms.moviechecker.domain.Schedule;
import com.prgrms.moviechecker.domain.TelegramProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TelegramService extends TelegramLongPollingBot {

    private final MovieCheckService movieCheckService;
    private final TelegramProperty telegramProperty;

    private String chatId;
    private String region;
    private String basarea;
    private String thea;
    private String schedule;

    @Override
    public String getBotUsername() {
        return telegramProperty.getUsername();
    }

    @Override
    public String getBotToken() {
        return telegramProperty.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String data = callbackQuery.getData(); // 버튼 데이터 추출
                log.info("callbackQuery.getChatInstance() = {}", callbackQuery.getChatInstance());
                log.info("callbackQuery.getData() = {}", callbackQuery.getData());
                log.info("callbackQuery.getId() = {}", callbackQuery.getId());
                log.info("callbackQuery.getInlineMessageId() = {}", callbackQuery.getInlineMessageId());

                if (Region.isRegion(data)) {
                    region = data;
                    List<AreaInformation> basareaCdList = movieCheckService.getBasareaCdList(region);

                    for (AreaInformation a : basareaCdList) {
                        log.info("basarea = {}", a.getCdNm());
                    }

                    proceedBasareaCd(basareaCdList, chatId);

                } else if (data.length() == 9){
                    basarea = data;

                    List<AreaInformation> theaCdList = movieCheckService.getTheaCdList(region, basarea);

                    for (AreaInformation a : theaCdList) {
                        log.info("theaCd = {}", a.getCdNm());
                    }

                    proceedTheaCd(theaCdList, chatId);
                } else if (data.length() == 6) {
                    thea = data;

                    getWantedDate();
                } else if (data.length() == 8) {
                    log.info("date = {}", data);
                    schedule = data;

                    List<Schedule> schedules = movieCheckService.getSchedule(thea, schedule);

                    for (Schedule a : schedules) {
                        log.info("getMovieCd = {}", a.getMovieCd());
                        log.info("getMovieNm = {}", a.getMovieNm());
                        log.info("getScheduleTimes = {}", a.getScheduleTimes());
                        log.info("--------------------------");
                    }
                }
            } else {
                handleCommands(update);
            }

        } catch (TelegramApiException | JsonProcessingException e) {
            log.info("onUpdateReceived()", e);
        }
    }

    private void proceedTheaCd(List<AreaInformation> theaCdList, String chatId) {
        SendMessage message = SendMessage.builder().chatId(chatId).text("영화관을 선택해주세요.").build();

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (int i = 0; i < theaCdList.size(); i++) {
            rowInline.add(new InlineKeyboardButton(theaCdList.get(i).getCdNm(), null, theaCdList.get(i).getCd(), null, null, null, null, null, null));
            rowsInline.add(new ArrayList<>(rowInline));
            rowInline.clear();
        }
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 명령어 핸들링 메서드
     *
     * @param update
     * @throws TelegramApiException
     */
    private void handleCommands(Update update) throws TelegramApiException {
        if (hasCommand(update)) {
            chatId = String.valueOf(update.getMessage().getChatId());
            String messageText = update.getMessage().getText();

            if (messageText.startsWith(Commands.LIST)) {
                processListCommand(chatId);
            }
        }
    }

    /**
     * 명령어(command)인지 확인하는 메서드
     *
     * @param update
     */
    private boolean hasCommand(Update update) {
        return update.getMessage().getEntities() != null && "bot_command".equals(update.getMessage().getEntities().get(0).getType());
    }

    /**
     * /list 처리 메서드
     *
     * @param chatId
     * @throws TelegramApiException
     */
    private void processListCommand(String chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder().chatId(chatId).text("지역을 선택해주세요.").build();

        List<String> regionNames = Region.getNames();

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        int cnt = 0;
        for (int i = 0; i < regionNames.size(); i++) {
            if (cnt == 3) {
                rowsInline.add(new ArrayList<>(rowInline));
                markupInline.setKeyboard(rowsInline);
                rowInline.clear();
                cnt = 0;
            }
            rowInline.add(new InlineKeyboardButton(regionNames.get(i), null, Region.findsWideareaCd(regionNames.get(i)), null, null, null, null, null, null));
            cnt++;
        }

        if (rowInline.size() > 0) {
            rowsInline.add(new ArrayList<>(rowInline));
            markupInline.setKeyboard(rowsInline);
        }

        message.setReplyMarkup(markupInline);

        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void proceedBasareaCd(List<AreaInformation> basareaCds, String chatId) {
        SendMessage message = SendMessage.builder().chatId(chatId).text("상세 지역을 선택해주세요.").build();

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        int cnt = 0;
        for (int i = 0; i < basareaCds.size(); i++) {
            if (cnt == 3) {
                rowsInline.add(new ArrayList<>(rowInline));
                markupInline.setKeyboard(rowsInline);
                rowInline.clear();
                cnt = 0;
            }
            rowInline.add(new InlineKeyboardButton(basareaCds.get(i).getCdNm(), null, basareaCds.get(i).getCd(), null, null, null, null, null, null));
            cnt++;
        }

        if (rowInline.size() > 0) {
            rowsInline.add(new ArrayList<>(rowInline));
            markupInline.setKeyboard(rowsInline);
        }

        message.setReplyMarkup(markupInline);

        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void getWantedDate() {
        SendMessage message = SendMessage.builder().chatId(chatId).text("조회를 원하시는 날짜를 선택해주세요.").build();

        String[] days = {"일", "월", "화", "수", "목", "금", "토"};

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat formatData = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

        List<String> dateForButton = new ArrayList<>();
        List<String> dateForData = new ArrayList<>();

        cal.add(Calendar.DAY_OF_MONTH, 0);
        dateForButton.add(format.format(cal.getTime()) + " " + days[(dayOfWeek) % 7]);
        dateForData.add(formatData.format(cal.getTime()));

        for (int i = 1; i <= 13; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            dateForButton.add(format.format(cal.getTime()) + " " + days[(dayOfWeek + i) % 7]);
            dateForData.add(formatData.format(cal.getTime()));
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        int cnt = 0;
        for (int i = 0; i < dateForButton.size(); i++) {
            if (cnt == 2) {
                rowsInline.add(new ArrayList<>(rowInline));
                markupInline.setKeyboard(rowsInline);
                rowInline.clear();
                cnt = 0;
            }
            rowInline.add(new InlineKeyboardButton(dateForButton.get(i), null, dateForData.get(i), null, null, null, null, null, null));
            cnt++;
        }

        if (rowInline.size() > 0) {
            rowsInline.add(new ArrayList<>(rowInline));
            markupInline.setKeyboard(rowsInline);
        }
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}