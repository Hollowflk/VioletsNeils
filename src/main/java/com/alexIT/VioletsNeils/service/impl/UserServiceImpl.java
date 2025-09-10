package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.repository.TgUserRepository;
import com.alexIT.VioletsNeils.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final TgUserRepository repository;

    public UserServiceImpl(TgUserRepository repository) {
        this.repository = repository;
    }

    public TgUserDto createUserDto(Update update) {
        if (!update.hasCallbackQuery()) {
            return new TgUserDto(
              update.getMessage().getFrom().getId(),
              update.getMessage().getChatId(),
              update.getMessage().getMessageId(),
              RoleUser.USER
            );
        }
        return new TgUserDto(
                update.getCallbackQuery().getFrom().getId(),
                update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getMessage().getMessageId(),
                RoleUser.USER
        );
    }

    @Override
    public Optional<TgUser> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public TgUser save(TgUserDto dto) {
        TgUser tgUser = new TgUser(
                dto.getUserId(),
                dto.getChatId(),
                dto.getMessageId(),
                dto.getRole()
        );
        repository.save(tgUser);
        return tgUser;
    }

    @Override
    public TgUser update(TgUser tgUser) {
        return repository.save(tgUser);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
