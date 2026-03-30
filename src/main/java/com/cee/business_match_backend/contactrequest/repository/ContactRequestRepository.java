package com.cee.business_match_backend.contactrequest.repository;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.contactrequest.model.ContactRequest;
import com.cee.business_match_backend.contactrequest.model.ContactRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {

    boolean existsByOfferIdAndSenderId(Long offerId, Long senderId);

    List<ContactRequest> findBySenderId(Long senderId);

    List<ContactRequest> findByOfferCreatorId(Long creatorId);

    List<ContactRequest> findByOfferId(Long offerId);

    List<ContactRequest> findByOfferIdAndSenderRole(Long offerId, Role senderRole);

    Optional<ContactRequest> findByIdAndOfferCreatorId(Long requestId, Long creatorId);

    long countByOfferIdAndSenderRoleAndStatus(Long offerId, Role senderRole, ContactRequestStatus status);

    List<ContactRequest> findByOfferIdAndSenderRoleAndIdNot(Long offerId, Role senderRole, Long excludedId);

}
