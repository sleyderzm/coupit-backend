package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Blockchain;
import com.allcode.coupit.models.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Currency findByHashAndBlockchain(String hash, Blockchain blockchain);
}
