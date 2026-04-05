package com.example.EventSphere.repo;

import com.example.EventSphere.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    public Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);
}
