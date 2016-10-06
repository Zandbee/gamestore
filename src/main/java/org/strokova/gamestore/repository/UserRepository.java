package org.strokova.gamestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.strokova.gamestore.model.User;

/**
 * @author vstrokova, 05.09.2016.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    User findByUsername(String username);
}
