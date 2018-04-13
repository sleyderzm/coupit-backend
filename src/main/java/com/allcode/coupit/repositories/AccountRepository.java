package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Account;
import com.allcode.coupit.models.Merchant;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> { }
