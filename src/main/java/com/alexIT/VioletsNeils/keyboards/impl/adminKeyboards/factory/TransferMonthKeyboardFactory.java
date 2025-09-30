package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory;

import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.TransferMonthKeyboardAdmin;
import org.springframework.stereotype.Component;

@Component
public class TransferMonthKeyboardFactory {
    public TransferMonthKeyboardAdmin create(String prefix, String backCallbackPrefix) {
        return new TransferMonthKeyboardAdmin(prefix, backCallbackPrefix);
    }
}
