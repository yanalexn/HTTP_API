package com.yan.http_api.repo;

import com.yan.http_api.entity.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Orderr, Long> {
    List<Orderr> findAllByClient_Id(Long id);
}
