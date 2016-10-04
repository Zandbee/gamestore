package org.strokova.gamestore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.model.Category;

/**
 * @author vstrokova, 12.09.2016.
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Modifying
    @Transactional
    @Query("update Application set downloadNumber = downloadNumber + 1 where id = ?1")
    int incrementDownloadNumber(int id);

    @Transactional
    Page<Application> findByCategory(Pageable request, Category category);

    @Transactional
    int countByCategory(Category category);
}
