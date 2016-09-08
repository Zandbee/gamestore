package org.strokova.gamestore.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import org.strokova.gamestore.model.User;

/**
 * @author vstrokova, 05.09.2016.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}
