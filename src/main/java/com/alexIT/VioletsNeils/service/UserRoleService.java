package com.alexIT.VioletsNeils.service;

import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.repository.TgUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private final TgUserRepository userRepository;

    public UserRoleService(TgUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RoleUser getRole(Long userId) {
        return userRepository.findById(userId)
                .map(TgUser::getRole)
                .orElse(RoleUser.USER);
    }
}
