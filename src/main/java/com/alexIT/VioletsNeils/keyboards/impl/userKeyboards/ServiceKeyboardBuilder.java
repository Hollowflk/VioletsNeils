package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards;

import com.alexIT.VioletsNeils.entity.Service;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.service.ServiceService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ServiceKeyboardBuilder implements KeyboardBuilder {

    private final int serviceCategoryId;
    private final ServiceService serviceService;
    private final String callbackPrefix;
    private final String backCallbackPrefix;

    public ServiceKeyboardBuilder(int serviceCategoryId, ServiceService serviceService, String callbackPrefix, String backCallbackPrefix) {
        this.serviceCategoryId = serviceCategoryId;
        this.serviceService = serviceService;
        this.callbackPrefix = callbackPrefix;
        this.backCallbackPrefix = backCallbackPrefix;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        List<Service> serviceList = serviceService.findAllByCategoryId(serviceCategoryId);
        for (Service service : serviceList) {
            rows.add(addButton(service.getName(), String.format(callbackPrefix, service.getId())));
        }
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(backCallbackPrefix)
                .build();
        rows.add(new InlineKeyboardRow(back));
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
