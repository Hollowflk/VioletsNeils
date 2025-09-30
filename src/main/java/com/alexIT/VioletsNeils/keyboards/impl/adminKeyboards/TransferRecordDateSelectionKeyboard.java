package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransferRecordDateSelectionKeyboard implements KeyboardBuilder {

    private final DailyRecordServiceImpl dailyRecordService;
    private final LocalDate selectedMonth;
    private final String callbackPrefix;
    private final String backCallbackPrefix;
    private static final String DESCRIPTION = """
            %d %s
            """;

    public TransferRecordDateSelectionKeyboard(DailyRecordServiceImpl dailyRecordService, LocalDate selectedMonth, String callbackPrefix, String backCallbackPrefix) {
        this.dailyRecordService = dailyRecordService;
        this.selectedMonth = selectedMonth;
        this.callbackPrefix = callbackPrefix;
        this.backCallbackPrefix = backCallbackPrefix;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        LocalDate start = LocalDate.of(selectedMonth.getYear(), selectedMonth.getMonth(), 1);
        LocalDate end = LocalDate.of(selectedMonth.getYear(), selectedMonth.getMonth(),
                MonthsAndDaysUtils.getDaysOfMonth(selectedMonth.getMonth(), selectedMonth.getYear()));

        List<DailyRecord> dailyRecords = dailyRecordService.findAllByDateBetween(start, end)
                .stream()
                .sorted()
                .toList();

        for (DailyRecord dailyRecord : dailyRecords) {
            if (dailyRecord.getTimeSlotList().isEmpty()) {
                continue;
            }
            String monthName = MonthsAndDaysUtils.getNameMonth(dailyRecord.getDate().getMonth().getValue());
            rows.add(addButton(String.format(DESCRIPTION,
                            dailyRecord.getDate().getDayOfMonth(),
                            MonthsAndDaysUtils.monthGenitiveForms.get(monthName)),
                    String.format(callbackPrefix, dailyRecord.getDate())));
        }
        rows.add(addButton("Назад", backCallbackPrefix));
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
