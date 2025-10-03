package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards;

import com.alexIT.VioletsNeils.entity.ServiceCategory;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.service.ServiceCategoryService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryKeyboard implements KeyboardBuilder {

    private final ServiceCategoryService serviceCategoryService;
    private final String callbackPrefix;
    private final String backCallbackPrefix;

    public ServiceCategoryKeyboard(ServiceCategoryService serviceCategoryService, String callbackPrefix, String backCallbackPrefix) {
        this.serviceCategoryService = serviceCategoryService;
        this.callbackPrefix = callbackPrefix;
        this.backCallbackPrefix = backCallbackPrefix;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        List<ServiceCategory> serviceCategoryList = serviceCategoryService.findAll();
        for (ServiceCategory category : serviceCategoryList) {
            rows.add(addButton(category.getName(), String.format(callbackPrefix, category.getId())));
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
