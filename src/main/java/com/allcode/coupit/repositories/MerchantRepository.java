package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Merchant;
import com.allcode.coupit.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MerchantRepository extends CrudRepository<Merchant, Long> {
    Page<Merchant> findByUser(User user, Pageable pageable);
}
