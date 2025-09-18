package com.alexIT.VioletsNeils.keyboards;

import com.alexIT.VioletsNeils.entity.ServiceCategory;
import com.alexIT.VioletsNeils.service.ServiceCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ServiceCategoryKeyboardBuilder implements KeyboardBuilder{

    private final ServiceCategoryService serviceCategoryService;

    public ServiceCategoryKeyboardBuilder(ServiceCategoryService serviceCategoryService) {
        this.serviceCategoryService = serviceCategoryService;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        List<ServiceCategory> serviceCategoryList = serviceCategoryService.findAll();
        for (ServiceCategory category : serviceCategoryList) {
            rows.add(addButton(category.getName(), String.format("/service_category_%d", category.getId())));
        }
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData("/menu")
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
