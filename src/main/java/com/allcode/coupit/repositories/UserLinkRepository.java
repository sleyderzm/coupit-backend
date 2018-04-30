package com.allcode.coupit.repositories;

import com.allcode.coupit.models.User;
import com.allcode.coupit.models.UserLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface UserLinkRepository extends CrudRepository<UserLink, Long> {
    UserLink findByUid(String uid);
    Page<UserLink> findByUserIn(User user, Pageable pageable);
}
