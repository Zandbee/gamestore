package org.strokova.gamestore.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.strokova.gamestore.model.Application;

import java.util.List;

/**
 * @author vstrokova, 12.09.2016.
 */
@Repository
public interface ApplicationRepository extends PagingAndSortingRepository<Application, Integer> {

    @Modifying
    @Transactional
    @Query("update Application set downloadNumber = downloadNumber + 1 where id = ?1")
    int incrementDownloadNumber(int id);
}
