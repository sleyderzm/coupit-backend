package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> { }
