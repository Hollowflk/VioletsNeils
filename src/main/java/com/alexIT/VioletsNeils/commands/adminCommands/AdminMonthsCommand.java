package com.alexIT.VioletsNeils.commands.adminCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.AdminDaysKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.AdminMonthKeyboardBuilder;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminMonthsCommand implements Command {

    private int year;
    private int month;
    private boolean isChooseAdminMonthCommand;
    private final AdminDaysKeyboardBuilder daysKeyboardBuilder;
    private final AdminMonthKeyboardBuilder monthKeyboardBuilder;
    private final DailyRecordServiceImpl dailyRecordService;

    @Override
    public boolean supports(String text, UserState state) {
        if (text != null && text.equals("/chooseAdminMonth") && state.equals(UserState.PREPARED)) {
            isChooseAdminMonthCommand = true;
            return true;
        }
        if (text != null && (text.startsWith("/admin_currentMonth") || text.startsWith("/admin_nextMonth")) && state.equals(UserState.PREPARED)) {
            String[] textArray = text.split("_");
            String[] currentDateArray = textArray[2].split("-");
            year = Integer.parseInt(currentDateArray[0]);
            month = Integer.parseInt(currentDateArray[1]);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (isChooseAdminMonthCommand) {
            return chooseMonth(userDto);
        }
       List<DailyRecord> records = getRecords();
        if (records.isEmpty()) {
            return recordsEmpty(userDto);
        }
        daysKeyboardBuilder.setRecords(records);
        InlineKeyboardMarkup keyboard = daysKeyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите запись")
                .replyMarkup(keyboard)
                .build();
    }

    private EditMessageText chooseMonth(TgUserDto dto) {
        InlineKeyboardMarkup keyboard = monthKeyboardBuilder.build();
        isChooseAdminMonthCommand = false;
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Выберите месяц")
                .replyMarkup(keyboard)
                .build();
    }

    private List<DailyRecord> getRecords() {
        Month currentMonth = Month.of(month);
        int daysInMonth = MonthsAndDaysUtils.getDaysOfMonth(currentMonth, year);
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, daysInMonth);
        return dailyRecordService.findAllByDateBetween(start, end)
                .stream()
                .sorted()
                .toList();
    }

    private EditMessageText recordsEmpty(TgUserDto dto) {
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Нет записей")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(
                                new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text("Назад")
                                                .callbackData("/getDateForNotifications")
                                                .build()
                                ))
                        )
                        .build())
                .build();
    }
}
