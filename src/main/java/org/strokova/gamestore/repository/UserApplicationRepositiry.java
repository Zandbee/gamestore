package org.strokova.gamestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.strokova.gamestore.model.UserApplication;
import org.strokova.gamestore.model.UserApplicationKey;

/**
 * @author vstrokova, 06.10.2016.
 */
@Repository
public interface UserApplicationRepositiry extends JpaRepository<UserApplication, UserApplicationKey> {
}
