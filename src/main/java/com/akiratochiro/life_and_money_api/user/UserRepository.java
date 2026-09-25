package com.akiratochiro.life_and_money_api.user;
import org.hibernate.internal.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}