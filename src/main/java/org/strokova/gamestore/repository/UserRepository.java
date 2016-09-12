package org.strokova.gamestore.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.strokova.gamestore.model.User;

/**
 * @author vstrokova, 05.09.2016.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}
