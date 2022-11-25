package com.yan.http_api.repo;

import com.yan.http_api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepo extends JpaRepository<Client, Long> {

    Optional<Client> findByName(String name);
}
