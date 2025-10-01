package com.alexIT.VioletsNeils.commands.adminCommands.CanselOrTransferRecordCommands.transferCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferMonthKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferRecordSelectionKeyboardFactory;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
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

    private final TransferMonthKeyboardFactory factory;
    private final TransferRecordSelectionKeyboardFactory transferRecordSelectionKeyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;
    private final UserSessionManager sessionManager;
    private static final String INFO_ABOUT_RECORD = """
            Имя: %s
            Номер телефона: %s
            Название услуги:
            %s
            Время услуги: %s
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.equals("/transferRecord_admin") || text.startsWith("/transferRecordDate_"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        if (userDto.getText().startsWith("/transferRecordDate_")) {
            String[] textArr = userDto.getText().split("_");
            String[] dateArr = textArr[1].split("-");
            LocalDate selectedDate = LocalDate.of(
                    Integer.parseInt(dateArr[0]),
                    Integer.parseInt(dateArr[1]),
                    Integer.parseInt(dateArr[2])
            );
            userSession.setSelectedDate(selectedDate);
            return selectRecord(userDto, selectedDate);
        }
        KeyboardBuilder keyboardBuilder = factory.create("transferRecord", "/canselOrTransferRecord");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите месяц для переноса.")
                .replyMarkup(keyboard)
                .build();
    }

    private EditMessageText selectRecord(TgUserDto dto, LocalDate selectedDate) {
        Optional<DailyRecord> optionalDailyRecord = dailyRecordService.findByDate(selectedDate);
        List<TimeSlot> timeSlotList = optionalDailyRecord.get().getTimeSlotList()
                .stream()
                .sorted()
                .toList();
        String backCallBack = selectedDate.getMonth().equals(LocalDate.now().getMonth()) ? "/transferRecordCurrentMonthAdmin" : "/transferRecordNextMonthAdmin";
        KeyboardBuilder keyboardBuilder = transferRecordSelectionKeyboardFactory.create(timeSlotList,
                "/selectedTransferRecord_%s", backCallBack);
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text(createMessage(timeSlotList, selectedDate))
                .replyMarkup(keyboard)
                .build();
    }

    private String createMessage(List<TimeSlot> timeSlotList, LocalDate selectedDate) {
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
