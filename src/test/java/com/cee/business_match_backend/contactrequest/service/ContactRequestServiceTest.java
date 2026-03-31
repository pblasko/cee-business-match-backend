package com.cee.business_match_backend.contactrequest.service;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.auth.repository.UserRepository;
import com.cee.business_match_backend.contactrequest.dto.UpdateContactRequestStatusRequest;
import com.cee.business_match_backend.contactrequest.model.ContactRequest;
import com.cee.business_match_backend.contactrequest.model.ContactRequestStatus;
import com.cee.business_match_backend.contactrequest.repository.ContactRequestRepository;
import com.cee.business_match_backend.offer.model.Offer;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.model.OfferStatus;
import com.cee.business_match_backend.offer.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactRequestServiceTest {


    @Mock
    private ContactRequestRepository contactRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private ContactRequestService contactRequestService;

    @Test
    void shouldSetOfferStatusToPartiallyMatchedWhenPrimaryPartnerAcceptedAndLegalSupportNeeded() {
        User exporter = User.builder()
                .id(1L)
                .email("exporter@test.com")
                .role(Role.EXPORTER)
                .build();

        User investor = User.builder()
                .id(2L)
                .email("investor@test.com")
                .role(Role.INVESTOR)
                .build();

        Offer offer = Offer.builder()
                .id(100L)
                .title("Expansion offer")
                .description("desc")
                .category(OfferCategory.EXPORT_EXPANSION)
                .targetRole(Role.INVESTOR)
                .country("Hungary")
                .needsLegalSupport(true)
                .status(OfferStatus.OPEN)
                .creator(exporter)
                .createdAt(LocalDateTime.now())
                .build();

        ContactRequest contactRequest = ContactRequest.builder()
                .id(10L)
                .offer(offer)
                .sender(investor)
                .senderRole(Role.INVESTOR)
                .message("Interested")
                .status(ContactRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        UpdateContactRequestStatusRequest request = new UpdateContactRequestStatusRequest();
        request.setStatus(ContactRequestStatus.ACCEPTED);

        when(userRepository.findByEmail("exporter@test.com")).thenReturn(Optional.of(exporter));
        when(contactRequestRepository.findByIdAndOfferCreatorId(10L, 1L)).thenReturn(Optional.of(contactRequest));
        when(contactRequestRepository.findByOfferIdAndSenderRoleAndIdNot(100L, Role.INVESTOR, 10L))
                .thenReturn(Collections.emptyList());

        when(contactRequestRepository.countByOfferIdAndSenderRoleAndStatus(100L, Role.INVESTOR, ContactRequestStatus.ACCEPTED))
                .thenReturn(1L);

        when(contactRequestRepository.countByOfferIdAndSenderRoleAndStatus(100L, Role.LAW_FIRM, ContactRequestStatus.ACCEPTED))
                .thenReturn(0L);

        contactRequestService.updateRequestStatus(10L, request, "exporter@test.com");

        assertEquals(OfferStatus.PARTIALLY_MATCHED, offer.getStatus());

        verify(offerRepository).save(offer);
        verify(contactRequestRepository).save(contactRequest);
    }

}
