package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.DaysKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.Month;
import java.util.Map;

@Component
public class CurrentMonthCommand implements Command{
    @Override
    public boolean supports(String text) {
        return text != null && text.equals("/currentMonth");
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        Map<String, Month> monthMap = MonthsAndDaysUtils.getMonthsAsValues();
        int daysInMonth = MonthsAndDaysUtils.getDaysOfMonth(monthMap.get("currentMonth"));
        KeyboardBuilder keyboardBuilder = new DaysKeyboardBuilder(daysInMonth);
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день.")
                .replyMarkup(keyboard)
                .build();
    }
}
