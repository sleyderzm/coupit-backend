package com.allcode.coupit.repositories;

import com.allcode.coupit.models.ProductImage;
import org.springframework.data.repository.CrudRepository;

public interface ProductImageRepository extends CrudRepository<ProductImage, Long> {
    ProductImage findByAwsKey(String awsKey);
}
