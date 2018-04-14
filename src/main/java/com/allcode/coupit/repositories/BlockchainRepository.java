package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Blockchain;
import org.springframework.data.repository.CrudRepository;

public interface BlockchainRepository extends CrudRepository<Blockchain, Long> {
    Blockchain findByName(String name);
}
