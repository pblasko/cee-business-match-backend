package com.cee.business_match_backend.offer.controller;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.common.dto.PagedResponse;
import com.cee.business_match_backend.offer.dto.CreateOfferRequest;
import com.cee.business_match_backend.offer.dto.OfferFilterRequest;
import com.cee.business_match_backend.offer.dto.OfferResponse;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.model.OfferStatus;
import com.cee.business_match_backend.offer.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public OfferResponse createOffer(@Valid @RequestBody CreateOfferRequest request,
                                     Authentication authentication) {
        return offerService.createOffer(request, authentication.getName());
    }

    @GetMapping
    public PagedResponse<OfferResponse> getOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) OfferCategory category,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Role targetRole,
            @RequestParam(required = false) OfferStatus status,
            @RequestParam(required = false) Boolean needsLegalSupport
    ) {
        OfferFilterRequest filter = OfferFilterRequest.builder()
                .category(category)
                .country(country)
                .targetRole(targetRole)
                .status(status)
                .needsLegalSupport(needsLegalSupport)
                .build();

        return offerService.getOffers(filter, page, size, sortBy, sortDir);
    }

    @GetMapping("/my")
    public List<OfferResponse> getMyOffers(Authentication authentication) {
        return offerService.getMyOffers(authentication.getName());
    }

}
