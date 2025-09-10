package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;

import java.util.Optional;

public interface UserService {

    Optional<TgUser> findById(Long id);

    TgUser save(TgUserDto dto);

    TgUser update(TgUser tgUser);

    void deleteById(Long id);
}
