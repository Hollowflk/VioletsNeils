package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory;

import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.ServiceCategoryKeyboard;
import com.alexIT.VioletsNeils.service.ServiceCategoryService;
import org.springframework.stereotype.Component;

@Component
public class ServiceCategoryKeyboardFactory {

    public ServiceCategoryKeyboard create(ServiceCategoryService serviceCategoryService, String callbackPrefix, String backCallbackPrefix) {
        return new ServiceCategoryKeyboard(serviceCategoryService, callbackPrefix, backCallbackPrefix);
    }
}
