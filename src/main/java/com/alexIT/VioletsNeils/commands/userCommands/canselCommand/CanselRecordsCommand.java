package com.alexIT.VioletsNeils.commands.userCommands.canselCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.CanselRecordKeyboard;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CanselRecordsCommand implements Command {

    private final TimeSlotService timeSlotService;
    private final CanselRecordKeyboard canselRecordKeyboard;
    private static final String INFO_ABOUT_RECORD = """
            Дата записи: %s
            Время записи: %s ч
            Название услуги: %s%n
            """;
    private Long timeSlotId;
    private boolean isCanselCommand;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        if (text != null && text.equals("/canselRecords") && (state.equals(UserState.PREPARED) || state.equals(UserState.COMPLETED)) && roleUser.equals(RoleUser.USER)) {
            return true;
        }
        if (text != null && text.startsWith("/canselRecord_") && (state.equals(UserState.PREPARED) || state.equals(UserState.COMPLETED)) && roleUser.equals(RoleUser.USER)) {
            timeSlotId = Long.parseLong(text.substring(14));
            isCanselCommand = true;
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (isCanselCommand) {
            String messageText;
            if (timeSlotService.deleteById(timeSlotId)) {
                messageText = "Запись успешно удалена";
            } else {
                messageText = "Не удалось удалить запись";
            }
            isCanselCommand = false;
            return EditMessageText.builder()
                    .chatId(userDto.getChatId())
                    .messageId(userDto.getMessageId())
                    .text(messageText)
                    .build();
        }
        List<TimeSlot> timeSlotList = timeSlotService.findAllByUserId(userDto.getUserId())
                .stream()
                .sorted()
                .toList();
        canselRecordKeyboard.setTimeSlotList(timeSlotList);
        InlineKeyboardMarkup keyboard = canselRecordKeyboard.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createTimeSlotList(timeSlotList))
                .replyMarkup(keyboard)
                .build();
    }

    private String createTimeSlotList(List<TimeSlot> timeSlotList) {
        if (timeSlotList.isEmpty()) {
            return "У вас нет записей для отмены";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Ваши записи.").append("\n\n");
        for (TimeSlot timeSlot : timeSlotList) {
            builder.append(String.format(INFO_ABOUT_RECORD,
                            timeSlot.getDailyRecord().getDate(),
                            timeSlot.getTime(),
                            timeSlot.getService().getName()))
                    .append("\n");
        }
        builder.append("Выберите запись для отмены.");
        return builder.toString();
    }
}
