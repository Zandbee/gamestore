package org.strokova.gamestore.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.strokova.gamestore.model.Application;

import java.util.List;

/**
 * @author vstrokova, 12.09.2016.
 */
@Repository
public interface ApplicationRepository extends PagingAndSortingRepository<Application, Integer> {
}
