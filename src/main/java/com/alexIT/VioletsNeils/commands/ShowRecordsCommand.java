package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowRecordsCommand implements Command{

    private final TimeSlotService timeSlotService;
    private static final String TIMESLOT_INFO = """
            День записи: %s
            Время записи: %s
            Название услуги:%n%s
            Продолжительность: %s%n
            """;

    @Override
    public boolean supports(String text) {
        return text != null && text.equals("/showRecords");
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createTimeList(userDto.getUserId()))
                .replyMarkup(keyboardMarkup())
                .build();
    }

    private String createTimeList(Long userId) {
        StringBuilder builder = new StringBuilder();
        List<TimeSlot> timeSlotList = timeSlotService.findAllByUserId(userId);
        timeSlotList = timeSlotList.stream().sorted().toList();
        builder.append("Ваши записи.").append("\n\n");
        for (TimeSlot timeSlot : timeSlotList) {
            builder.append(String.format(TIMESLOT_INFO,
                    timeSlot.getDailyRecord().getDate(),
                    timeSlot.getTime(),
                    timeSlot.getService().getName(),
                    timeSlot.getService().getDuration()));
        }
        builder.append("\n");
        return builder.toString();
    }

    private InlineKeyboardMarkup keyboardMarkup() {
        InlineKeyboardRow row = new InlineKeyboardRow();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData("/menu")
                .build();
        row.add(backButton);
        return InlineKeyboardMarkup.builder().keyboard(List.of(row)).build();
    }
}
