package com.rhaydae.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhaydae.entities.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long > {
	
	boolean existsByCandidate_IdAndJobOffers_Id(Long candidateId, Long jobOfferId);

    List<Application> findByCandidate_Id(Long candidateId);

    List<Application> findByJobOffers_Id(Long jobOfferId);

}
