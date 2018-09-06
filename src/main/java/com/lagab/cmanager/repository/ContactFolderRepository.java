package com.lagab.cmanager.repository;

import com.lagab.cmanager.domain.ContactFolder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ContactFolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactFolderRepository extends JpaRepository<ContactFolder, Long> {

}
