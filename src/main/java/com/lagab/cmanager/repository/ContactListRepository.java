package com.lagab.cmanager.repository;

import com.lagab.cmanager.domain.ContactList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ContactList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactListRepository extends JpaRepository<ContactList, Long>, JpaSpecificationExecutor<ContactList> {

}
