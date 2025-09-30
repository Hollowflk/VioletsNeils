package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards;

import org.springframework.stereotype.Component;

@Component
public class ConfirmKeyboardFactory {

    public ConfirmKeyboardBuilder create(String callbackPrefix, String backCallbackPrefix) {
        return new ConfirmKeyboardBuilder(callbackPrefix, backCallbackPrefix);
    }

}
