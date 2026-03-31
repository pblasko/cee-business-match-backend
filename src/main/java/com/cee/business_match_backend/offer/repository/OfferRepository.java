package com.cee.business_match_backend.offer.repository;

import com.cee.business_match_backend.offer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    List<Offer> findByCreatorId(Long creatorId);

}
