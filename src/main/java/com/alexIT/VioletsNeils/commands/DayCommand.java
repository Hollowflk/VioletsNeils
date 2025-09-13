package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.DaysKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DayCommand implements Command {

    private static final String INFO = """
            Вы выбрали дату: %d %s.
            Выберите время для записи
            """;
    private int day;
    private String monthName;

    @Override
    public boolean supports(String text) {
        Pattern pattern = Pattern.compile("^/day([1-9]|[12][0-9]|3[01])\\s+(январь|февраль|март|апрель|май|июнь|июль|август|сентябрь|октябрь|ноябрь|декабрь)$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            day = Integer.parseInt(matcher.group(1));
            monthName = matcher.group(2);
            monthName = MonthsAndDaysUtils.monthGenitiveForms.get(monthName);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        KeyboardBuilder keyboardBuilder = new DaysKeyboardBuilder(day, monthName);
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        // TODO: Убрать
        System.out.println(userDto.getService());
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(String.format(INFO, day, monthName))
                .replyMarkup(keyboard)
                .build();
    }
}
