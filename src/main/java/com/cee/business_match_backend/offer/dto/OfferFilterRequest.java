package com.cee.business_match_backend.offer.dto;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.model.OfferStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OfferFilterRequest {

    private OfferCategory category;
    private String country;
    private Role targetRole;
    private OfferStatus status;
    private Boolean needsLegalSupport;

}
