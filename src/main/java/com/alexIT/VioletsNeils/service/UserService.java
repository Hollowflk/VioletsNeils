package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.entity.TgUser;

import java.util.Optional;

public interface UserService {

    Optional<TgUser> findById(Long id);

    TgUser save(TgUser user);

    TgUser update(TgUser tgUser);

    void deleteById(Long id);
}
