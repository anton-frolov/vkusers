package com.example.vkusers.repository;

import com.example.vkusers.domain.VkUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VkUserRepository extends JpaRepository<VkUser, Long> {
}
