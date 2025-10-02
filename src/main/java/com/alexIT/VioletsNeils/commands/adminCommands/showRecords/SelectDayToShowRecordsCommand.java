package com.alexIT.VioletsNeils.commands.adminCommands.showRecords;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferRecordSelectionKeyboardFactory;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectDayToShowRecordsCommand implements Command {

    private final DailyRecordServiceImpl dailyRecordService;
    private static final String INFO_ABOUT_RECORD = """
            Имя: %s
            Номер телефона: %s
            Название услуги:
            %s
            Время записи: %s
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.startsWith("/showRecordForAdmin_")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        String[] textArr = userDto.getText().split("_");
        String[] splitTextArr = textArr[1].split("-");
        LocalDate selectedDate = LocalDate.of(
                Integer.parseInt(splitTextArr[0]),
                Integer.parseInt(splitTextArr[1]),
                Integer.parseInt(splitTextArr[2])
        );
        List<TimeSlot> timeSlotList = dailyRecordService.findByDate(selectedDate).orElseThrow().getTimeSlotList()
                .stream()
                .sorted()
                .toList();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createMessage(timeSlotList))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(
                                List.of(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton.builder()
                                                        .text("Назад")
                                                        .callbackData("/showRecordsCurrentMonthAdmin")
                                                        .build()
                                        )
                                )
                        )
                        .build())
                .build();
    }

    private String createMessage(List<TimeSlot> timeSlotList) {
        StringBuilder builder = new StringBuilder();
        builder.append("Список записей на этот день.").append("\n\n");
        for (TimeSlot timeSlot : timeSlotList) {
            builder.append(String.format(INFO_ABOUT_RECORD,
                    timeSlot.getUser().getFullName(),
                    timeSlot.getUser().getPhoneNumber(),
                    timeSlot.getService().getName(),
                    timeSlot.getTime()));
            builder.append("\n");
        }
        return builder.toString();
    }
}
