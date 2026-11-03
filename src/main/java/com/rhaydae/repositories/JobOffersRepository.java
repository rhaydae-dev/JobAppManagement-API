package com.rhaydae.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhaydae.entities.JobOffers;

@Repository
public interface JobOffersRepository extends JpaRepository<JobOffers, Long> {

}
