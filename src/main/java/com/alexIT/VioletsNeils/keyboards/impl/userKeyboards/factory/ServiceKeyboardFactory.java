package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory;

import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.ServiceKeyboardBuilder;
import com.alexIT.VioletsNeils.service.ServiceService;
import org.springframework.stereotype.Component;

@Component
public class ServiceKeyboardFactory {

    public ServiceKeyboardBuilder create(int serviceCategoryId, ServiceService serviceService, String callbackPrefix, String backCallbackPrefix) {
        return new ServiceKeyboardBuilder(serviceCategoryId, serviceService, callbackPrefix, backCallbackPrefix);
    }
}
