package com.cee.business_match_backend.contactrequest.service;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.auth.repository.UserRepository;
import com.cee.business_match_backend.common.exception.BusinessException;
import com.cee.business_match_backend.contactrequest.dto.ContactRequestResponse;
import com.cee.business_match_backend.contactrequest.dto.CreateContactRequest;
import com.cee.business_match_backend.contactrequest.dto.UpdateContactRequestStatusRequest;
import com.cee.business_match_backend.contactrequest.model.ContactRequest;
import com.cee.business_match_backend.contactrequest.model.ContactRequestStatus;
import com.cee.business_match_backend.contactrequest.repository.ContactRequestRepository;
import com.cee.business_match_backend.offer.model.Offer;
import com.cee.business_match_backend.offer.model.OfferStatus;
import com.cee.business_match_backend.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    public ContactRequestResponse createRequest(CreateContactRequest request, String userEmail) {
        User sender = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Offer offer = offerRepository.findById(request.getOfferId())
                .orElseThrow(() -> new BusinessException("Offer not found"));

        validateCreateRequest(sender, offer);

        if (contactRequestRepository.existsByOfferIdAndSenderId(offer.getId(), sender.getId())) {
            throw new BusinessException("You have already sent a contact request for this offer");
        }

        ContactRequest contactRequest = ContactRequest.builder()
                .offer(offer)
                .sender(sender)
                .senderRole(sender.getRole())
                .message(request.getMessage())
                .status(ContactRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(contactRequestRepository.save(contactRequest));
    }

    public List<ContactRequestResponse> getSentRequests(String userEmail) {
        User sender = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return contactRequestRepository.findBySenderId(sender.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ContactRequestResponse> getReceivedRequests(String userEmail) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return contactRequestRepository.findByOfferCreatorId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public ContactRequestResponse updateRequestStatus(Long requestId,
                                                      UpdateContactRequestStatusRequest request,
                                                      String userEmail) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ContactRequest contactRequest = contactRequestRepository.findByIdAndOfferCreatorId(requestId, owner.getId())
                .orElseThrow(() -> new BusinessException("Contact request not found or access denied"));

        validateStatusUpdate(request.getStatus());

        contactRequest.setStatus(request.getStatus());
        contactRequestRepository.save(contactRequest);

        if (request.getStatus() == ContactRequestStatus.ACCEPTED) {
            rejectOtherRequestsOfSameRole(contactRequest);
        }

        recalculateOfferStatus(contactRequest.getOffer());

        return mapToResponse(contactRequest);
    }

    private void validateCreateRequest(User sender, Offer offer) {
        Role senderRole = sender.getRole();
        Role creatorRole = offer.getCreator().getRole();
        Role targetRole = offer.getTargetRole();

        if (offer.getCreator().getId().equals(sender.getId())) {
            throw new BusinessException("You cannot send a contact request to your own offer");
        }

        if (senderRole == Role.EXPORTER && creatorRole == Role.EXPORTER) {
            throw new BusinessException("Exporters cannot apply to exporter offers");
        }

        if (senderRole == Role.INVESTOR && creatorRole == Role.INVESTOR) {
            throw new BusinessException("Investors cannot apply to investor offers");
        }

        if (senderRole != Role.LAW_FIRM && senderRole != targetRole) {
            throw new BusinessException("Only the primary target role or a law firm can apply to this offer");
        }
    }

    private void validateStatusUpdate(ContactRequestStatus status) {
        if (status != ContactRequestStatus.ACCEPTED && status != ContactRequestStatus.REJECTED) {
            throw new BusinessException("Only ACCEPTED or REJECTED status updates are allowed");
        }
    }

    private void rejectOtherRequestsOfSameRole(ContactRequest acceptedRequest) {
        List<ContactRequest> others = contactRequestRepository.findByOfferIdAndSenderRoleAndIdNot(
                acceptedRequest.getOffer().getId(),
                acceptedRequest.getSenderRole(),
                acceptedRequest.getId()
        );

        for (ContactRequest other : others) {
            other.setStatus(ContactRequestStatus.REJECTED);
        }

        contactRequestRepository.saveAll(others);
    }

    private void recalculateOfferStatus(Offer offer) {
        Role primaryRole = offer.getTargetRole();

        long acceptedPrimary = contactRequestRepository.countByOfferIdAndSenderRoleAndStatus(
                offer.getId(), primaryRole, ContactRequestStatus.ACCEPTED
        );

        long acceptedLawFirm = contactRequestRepository.countByOfferIdAndSenderRoleAndStatus(
                offer.getId(), Role.LAW_FIRM, ContactRequestStatus.ACCEPTED
        );

        if (acceptedPrimary == 0) {
            offer.setStatus(OfferStatus.OPEN);
        } else if (!offer.isNeedsLegalSupport()) {
            offer.setStatus(OfferStatus.MATCHED);
        } else if (acceptedLawFirm == 0) {
            offer.setStatus(OfferStatus.PARTIALLY_MATCHED);
        } else {
            offer.setStatus(OfferStatus.MATCHED);
        }

        offerRepository.save(offer);
    }

    private ContactRequestResponse mapToResponse(ContactRequest request) {
        return ContactRequestResponse.builder()
                .id(request.getId())
                .offerId(request.getOffer().getId())
                .offerTitle(request.getOffer().getTitle())
                .senderId(request.getSender().getId())
                .senderEmail(request.getSender().getEmail())
                .senderRole(request.getSenderRole())
                .message(request.getMessage())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .build();
    }
}
