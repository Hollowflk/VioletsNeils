package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.convector.ConvectorUser;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalDate;

@Component
public class ConfirmCommand implements Command {

    private final UserSessionManager sessionManager;
    private final DailyRecordServiceImpl dailyRecordService;
    private final ConvectorUser convectorUser;
    private final UserServiceImpl userService;

    public ConfirmCommand(UserSessionManager sessionManager, DailyRecordServiceImpl dailyRecordService, ConvectorUser convectorUser, UserServiceImpl userService) {
        this.sessionManager = sessionManager;
        this.dailyRecordService = dailyRecordService;
        this.convectorUser = convectorUser;
        this.userService = userService;
    }

    @Override
    public boolean supports(String text) {
        return text != null && text.equals("/confirm");
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession session = sessionManager.getOrCreateSession(userDto.getUserId());

        TgUser user = getOrCreateUser(userDto, session);
        DailyRecord dailyRecord = getOrCreateDailyRecord(session.getSelectedDate());

        createAndAddTimeSlot(dailyRecord, session, user);
        dailyRecordService.save(dailyRecord);

        sessionManager.removeSession(user.getUserId());
        return buildSuccessMessage(userDto);
    }

    private TgUser getOrCreateUser(TgUserDto dto, UserSession session) {
        return userService.findById(session.getUserId())
                .orElseGet(() -> {
                    TgUser newUser = convectorUser.convertDtoInUser(dto);
                    return userService.save(newUser);
                });
    }

    private DailyRecord getOrCreateDailyRecord(LocalDate date) {
        return dailyRecordService.findByDate(date)
                .orElseGet(() -> dailyRecordService.save(new DailyRecord(date)));
    }

    private void createAndAddTimeSlot(DailyRecord record, UserSession session, TgUser user) {
        TimeSlot slot = new TimeSlot(
                session.getSelectedTime(),
                record,
                session.getSelectedService(),
                user
        );
        record.getTimeSlotList().add(slot);
    }

    private EditMessageText buildSuccessMessage(TgUserDto dto) {
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Вы успешно записаны!")
                .build();
    }
}
