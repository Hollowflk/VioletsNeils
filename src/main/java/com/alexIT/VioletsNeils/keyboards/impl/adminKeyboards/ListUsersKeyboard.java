package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ListUsersKeyboard implements KeyboardBuilder {

    private final List<TgUser> users;
    private final int currentPage;
    private final int pageSize;
    private final String callbackPrefix;

    private static final String INFO_USER = """
            %s : %s
            """;

    public ListUsersKeyboard(List<TgUser> users, int currentPage, int pageSize, String callbackPrefix) {
        this.users = users;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.callbackPrefix = callbackPrefix;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        int totalPages = (int) Math.ceil((double) users.size() / pageSize);
        int fromIndex = currentPage * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, users.size());

        List<TgUser> pageUsers = users.subList(fromIndex, toIndex);

        for (TgUser user : pageUsers) {
            rows.add(addButton(String.format(
                    INFO_USER,
                    user.getFullName(),
                    user.getPhoneNumber()), "/selectUser_" + user.getUserId()));
        }

        InlineKeyboardRow navRow = new InlineKeyboardRow();

        if (currentPage > 0) {
            navRow.add(InlineKeyboardButton.builder()
                    .text("Назад")
                    .callbackData(callbackPrefix + (currentPage - 1))
                    .build());
        }

        if (currentPage < totalPages - 1) {
            navRow.add(InlineKeyboardButton.builder()
                    .text("Вперед")
                    .callbackData(callbackPrefix + (currentPage + 1))
                    .build());
        }

        if (!navRow.isEmpty()) {
            rows.add(navRow);
        }

        rows.add(addButton("Меню", "/menu"));

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    @Override
    public InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
