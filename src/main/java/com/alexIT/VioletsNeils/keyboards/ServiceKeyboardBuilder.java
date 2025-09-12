package com.alexIT.VioletsNeils.keyboards;

import com.alexIT.VioletsNeils.entity.Service;
import com.alexIT.VioletsNeils.service.ServiceService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ServiceKeyboardBuilder implements KeyboardBuilder{

    private final int serviceCategoryId;
    private final ServiceService serviceService;

    public ServiceKeyboardBuilder(int serviceCategoryId, ServiceService serviceService) {
        this.serviceCategoryId = serviceCategoryId;
        this.serviceService = serviceService;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        List<Service> serviceList = serviceService.findAllByCategoryId(serviceCategoryId);
        for (Service service : serviceList) {
            rows.add(addButton(service.getName(), String.format("/service_id%d", service.getId())));
        }
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData("/signUp")
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
