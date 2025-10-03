package com.alexIT.VioletsNeils.commands.userCommands.signUpCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.Service;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.ServiceKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.ServiceKeyboardFactory;
import com.alexIT.VioletsNeils.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceCategoryCommand implements Command {

    private final ServiceService serviceService;
    private final ServiceKeyboardFactory serviceKeyboardFactory;
    private static final String SERVICES_INFO = """
            Наименование услуги:%n%s
            Цена услуги: %s руб.
            Продолжительность: %s
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.startsWith("/service_category_")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.USER);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        int serviceCategoryId = Integer.parseInt(userDto.getText().split("_")[2]);
        KeyboardBuilder keyboardBuilder = serviceKeyboardFactory.create(serviceCategoryId, serviceService,
                "/service_id%d", "/signUp");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createListInformationAboutServices(serviceCategoryId))
                .replyMarkup(keyboard)
                .build();
    }

    private String createListInformationAboutServices(int serviceCategoryId) {
        StringBuilder builder = new StringBuilder();
        List<Service> serviceList = serviceService.findAllByCategoryId(serviceCategoryId);
        builder.append("Список услуг.").append("\n");
        for (Service service : serviceList) {
            builder.append(String.format(SERVICES_INFO,
                            service.getName(),
                            service.getPrice(),
                            service.getDuration()))
                    .append("\n");
        }
        builder.append("Выберите услугу.");
        return builder.toString();
    }
}
