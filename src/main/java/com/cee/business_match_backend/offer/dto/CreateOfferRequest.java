package com.cee.business_match_backend.offer.dto;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.offer.model.OfferCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOfferRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private OfferCategory category;

    @NotNull
    private Role targetRole;

    @NotBlank
    private String country;

    private boolean needsLegalSupport;

}
