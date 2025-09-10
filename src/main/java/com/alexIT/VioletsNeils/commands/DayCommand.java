package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DayCommand implements Command{

    private int day;
    private String monthName;


    @Override
    public boolean supports(String text) {
        Pattern pattern = Pattern.compile("^/day([1-9]|[12][0-9]|3[01])\\s+(январь|февраль|март|апрель|май|июнь|июль|август|сентябрь|октябрь|ноябрь|декабрь)$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            day = Integer.parseInt(matcher.group(1));
            monthName = matcher.group(2);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(MessageFormat.format("Вы выбрали дату для записи: {0} {1}", day, MonthsAndDaysUtils.monthGenitiveForms.get(monthName)))
                .build();
    }
}
