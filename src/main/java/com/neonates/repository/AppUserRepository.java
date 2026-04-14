package com.neonates.repository;

import com.neonates.Enum.UserRole;
import com.neonates.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByUserUuid(String uuid);

    List<AppUser> findByPrimaryRole(UserRole role);

    List<AppUser> findByActiveFlagTrue();
}
