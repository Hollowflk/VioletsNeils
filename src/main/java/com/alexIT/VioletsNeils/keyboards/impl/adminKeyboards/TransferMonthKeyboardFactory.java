package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import org.springframework.stereotype.Component;

@Component
public class TransferMonthKeyboardFactory {
    public TransferMonthKeyboardAdmin create(String prefix, String backCallbackPrefix) {
        return new TransferMonthKeyboardAdmin(prefix, backCallbackPrefix);
    }
}
