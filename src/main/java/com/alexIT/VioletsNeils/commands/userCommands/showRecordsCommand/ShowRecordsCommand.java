package com.alexIT.VioletsNeils.commands.userCommands.showRecordsCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShowRecordsCommand implements Command {

    private final TimeSlotService timeSlotService;
    private final TelegramClient telegramClient;
    private static final String TIMESLOT_INFO = """
            День записи: %s %s
            Время записи: %s ч
            Название услуги:%n%s
            Продолжительность: %s%n
            """;

    @Override
    public boolean supports(String text, UserState state) {
        return text != null && text.equals("/showRecords") && state.equals(UserState.PREPARED);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        getRecords(userDto.getUserId());
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text("Вернуться назад")
                .replyMarkup(keyboardMarkup())
                .build();
    }

    private void getRecords(Long userId) {
        List<TimeSlot> timeSlotList = timeSlotService.findAllByUserId(userId)
                .stream()
                .sorted()
                .toList();

        SendMessage yourRecordText;
        if (timeSlotList.isEmpty()) {
            yourRecordText = SendMessage.builder()
                    .chatId(userId)
                    .text("У вас нет записей")
                    .build();
            sendMessage(userId, yourRecordText);
            return;
        } else {
            yourRecordText = SendMessage.builder()
                    .chatId(userId)
                    .text("Ваши записи")
                    .build();
        }
        sendMessage(userId, yourRecordText);

        for (int i = 0; i < timeSlotList.size(); i++) {
            TimeSlot currentRecord = timeSlotList.get(i);
            if (i > 0) {
                TimeSlot previousRecord = timeSlotList.get(i - 1);
                if (currentRecord.getService().getId().equals(previousRecord.getService().getId())
                        && currentRecord.getDailyRecord().getDate().equals(previousRecord.getDailyRecord().getDate())) {
                    continue;
                }
            }
            SendMessage msg = createMessage(currentRecord);
            sendMessage(userId, msg);
        }
    }

    private SendMessage createMessage(TimeSlot timeSlot) {
        String monthName = MonthsAndDaysUtils.getNameMonth(timeSlot.getDailyRecord().getDate().getMonth().getValue());
        return SendMessage.builder()
                .chatId(timeSlot.getUser().getUserId())
                .text(String.format(TIMESLOT_INFO,
                        timeSlot.getDailyRecord().getDate().getDayOfMonth(),
                        MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                        timeSlot.getTime(),
                        timeSlot.getService().getName(),
                        timeSlot.getService().getDuration()))
                .build();
    }

    private void sendMessage(Long userId, SendMessage message) {
        try {
            telegramClient.execute(message);
            Thread.sleep(35);
        } catch (TelegramApiException e) {
            log.warn("Не удалось показать запись для ID: {}, {}", userId, e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private InlineKeyboardMarkup keyboardMarkup() {
        InlineKeyboardRow row = new InlineKeyboardRow();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Меню")
                .callbackData("/menu")
                .build();
        row.add(backButton);
        return InlineKeyboardMarkup.builder().keyboard(List.of(row)).build();
    }
}
