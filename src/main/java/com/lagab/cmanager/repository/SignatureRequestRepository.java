package com.lagab.cmanager.repository;

import com.lagab.cmanager.domain.SignatureRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SignatureRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignatureRequestRepository extends JpaRepository<SignatureRequest, Long> {

}
