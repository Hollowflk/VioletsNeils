package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.TransferMonthKeyboardAdmin;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.TransferRecordSelectionKeyboard;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferRecordCommand implements Command {

    private final TransferMonthKeyboardAdmin transferMonthKeyboardAdmin;
    private final TransferRecordSelectionKeyboard selectionKeyboard;
    private final DailyRecordServiceImpl dailyRecordService;
    private boolean isTransferRecord;
    private LocalDate selectedDate;
    private static final String INFO_ABOUT_RECORD = """
            Имя: %s
            Номер телефона: %s
            Название услуги:
            %s
            Время услуги: %s
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        if (text != null && text.equals("/transferRecord_admin") && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.ADMIN)) {
            return true;
        }
        if (text != null && text.startsWith("/transferRecord_") && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.ADMIN)) {
            String[] textArr = text.split("_");
            String[] dateArr = textArr[1].split("-");
            selectedDate = LocalDate.of(
                    Integer.parseInt(dateArr[0]),
                    Integer.parseInt(dateArr[1]),
                    Integer.parseInt(dateArr[2])
            );
            isTransferRecord = true;
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (isTransferRecord) {
            return selectRecord(userDto);
        }
        InlineKeyboardMarkup keyboard = transferMonthKeyboardAdmin.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите месяц.")
                .replyMarkup(keyboard)
                .build();
    }

    private EditMessageText selectRecord(TgUserDto dto) {
        Optional<DailyRecord> optionalDailyRecord = dailyRecordService.findByDate(selectedDate);
        List<TimeSlot> timeSlotList = optionalDailyRecord.get().getTimeSlotList()
                .stream()
                .sorted()
                .toList();
        selectionKeyboard.setTimeSlotList(timeSlotList);
        InlineKeyboardMarkup keyboard = selectionKeyboard.build();
        isTransferRecord = false;
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text(createMessage(timeSlotList))
                .replyMarkup(keyboard)
                .build();
    }

    private String createMessage(List<TimeSlot> timeSlotList) {
        StringBuilder builder = new StringBuilder();
        builder.append("Вы выбрали день: ").append(selectedDate.toString()).append("\n");
        builder.append("Выберите время для переноса.").append("\n\n");
        for (int i = 0; i < timeSlotList.size(); i++) {
            TimeSlot currentRecord = timeSlotList.get(i);
            if (i > 0) {
                TimeSlot previousRecord = timeSlotList.get(i - 1);
                if (currentRecord.getService().getId().equals(previousRecord.getService().getId())
                        && currentRecord.getDailyRecord().getDate().equals(previousRecord.getDailyRecord().getDate())) {
                    continue;
                }
            }
            builder.append(String.format(INFO_ABOUT_RECORD,
                    currentRecord.getUser().getFullName(),
                    currentRecord.getUser().getPhoneNumber(),
                    currentRecord.getService().getName(),
                    currentRecord.getTime()));
            builder.append("\n");
        }
        return builder.toString();
    }
}
