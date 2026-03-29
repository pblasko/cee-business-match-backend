package com.cee.business_match_backend.offer.dto;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.model.OfferStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OfferResponse {

    private Long id;
    private String title;
    private String description;
    private OfferCategory category;
    private Role targetRole;
    private String country;
    private boolean needsLegalSupport;
    private OfferStatus status;
    private Long creatorId;
    private String creatorEmail;
    private LocalDateTime createdAt;

}
