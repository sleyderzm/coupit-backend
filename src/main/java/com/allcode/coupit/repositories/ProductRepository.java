package com.allcode.coupit.repositories;

import com.allcode.coupit.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> { }
