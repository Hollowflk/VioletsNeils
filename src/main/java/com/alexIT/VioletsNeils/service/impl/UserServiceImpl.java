package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.repository.TgUserRepository;
import com.alexIT.VioletsNeils.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    private final TgUserRepository repository;

    public UserServiceImpl(TgUserRepository repository) {
        this.repository = repository;
    }

    public TgUserDto createUserDto(Update update) {
        Long userId;
        Long chatId;
        Integer messageId;

        if (!update.hasCallbackQuery()) {
            userId = update.getMessage().getFrom().getId();
            chatId = update.getMessage().getChatId();
            messageId = update.getMessage().getMessageId();
        } else {
            userId = update.getCallbackQuery().getFrom().getId();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
        }

        return new TgUserDto(userId, chatId, messageId, RoleUser.USER);
    }

    @Override
    public Optional<TgUser> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public TgUser save(TgUserDto dto) {
        TgUser tgUser = new TgUser(
                dto.getUserId(),
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
