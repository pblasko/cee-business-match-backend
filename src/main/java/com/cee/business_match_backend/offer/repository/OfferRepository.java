package com.cee.business_match_backend.offer.repository;

import com.cee.business_match_backend.offer.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByCreatorId(Long creatorId);

}
