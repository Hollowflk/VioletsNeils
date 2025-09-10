package com.alexIT.VioletsNeils.repository;

import com.alexIT.VioletsNeils.entity.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgUserRepository extends JpaRepository<TgUser, Long> {
}
