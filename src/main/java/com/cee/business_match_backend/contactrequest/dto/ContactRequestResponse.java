package com.cee.business_match_backend.contactrequest.dto;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.contactrequest.model.ContactRequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContactRequestResponse {

    private Long id;
    private Long offerId;
    private String offerTitle;

    private Long senderId;
    private String senderEmail;
    private Role senderRole;

    private String message;
    private ContactRequestStatus status;
    private LocalDateTime createdAt;

}
