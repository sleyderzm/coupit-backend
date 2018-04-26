package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Blockchain;
import com.allcode.coupit.models.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Currency findByHashAndBlockchain(String hash, Blockchain blockchain);
    Page<Currency> findAllByOrderByName(Pageable pageable);
    List<Currency> findAllByOrderByName();
}
