package com.yan.http_api.repo;

import com.yan.http_api.entity.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orderr, Long> {
}
