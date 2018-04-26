package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Merchant;
import com.allcode.coupit.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Page<Product> findByMerchantIn(Set<Merchant> merchants, Pageable pageable);
}
