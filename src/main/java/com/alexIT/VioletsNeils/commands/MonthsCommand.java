package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.DaysKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.repository.DailyRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.Month;

@Component
public class MonthsCommand implements Command{

    private int year;
    private int month;
    private final DailyRepository dailyRepository;

    public MonthsCommand(DailyRepository dailyRepository) {
        this.dailyRepository = dailyRepository;
    }

    @Override
    public boolean supports(String text) {
        if (text != null && (text.startsWith("/currentMonth") || text.startsWith("/nextMonth"))) {
            String[] textArr = text.split("_");
            String[] currentDateArray = textArr[1].split("-");
            year = Integer.parseInt(currentDateArray[0]);
            month = Integer.parseInt(currentDateArray[1]);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        Month currentMonth = Month.of(month);
        KeyboardBuilder keyboardBuilder = new DaysKeyboardBuilder(dailyRepository,year, currentMonth);
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день.")
                .replyMarkup(keyboard)
                .build();
    }
}
