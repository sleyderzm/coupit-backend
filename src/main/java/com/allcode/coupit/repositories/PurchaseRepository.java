package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Blockchain;
import com.allcode.coupit.models.Purchase;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> { }