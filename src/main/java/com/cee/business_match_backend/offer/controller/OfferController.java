package com.cee.business_match_backend.offer.controller;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.common.dto.PagedResponse;
import com.cee.business_match_backend.offer.dto.CreateOfferRequest;
import com.cee.business_match_backend.offer.dto.OfferFilterRequest;
import com.cee.business_match_backend.offer.dto.OfferResponse;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.model.OfferStatus;
import com.cee.business_match_backend.offer.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@Tag(name = "Offers", description = "Offer management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OfferController {

    private final OfferService offerService;

    @Operation(summary = "Create offer", description = "Creates a new offer. Only INVESTOR and EXPORTER users are allowed to create offers.")
    @PostMapping
    public OfferResponse createOffer(@Valid @RequestBody CreateOfferRequest request,
                                     Authentication authentication) {
        return offerService.createOffer(request, authentication.getName());
    }

    @Operation(summary = "Search offers", description = "Returns paginated offers with filtering and sorting support")
    @GetMapping
    public PagedResponse<OfferResponse> getOffers(
            @Parameter(description = "Zero-based page index") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size between 1 and 100") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field: createdAt, title, country, status") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction: asc or desc") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Offer category filter") @RequestParam(required = false) OfferCategory category,
            @Parameter(description = "Country filter") @RequestParam(required = false) String country,
            @Parameter(description = "Primary target role filter") @RequestParam(required = false) Role targetRole,
            @Parameter(description = "Offer status filter") @RequestParam(required = false) OfferStatus status,
            @Parameter(description = "Legal support filter") @RequestParam(required = false) Boolean needsLegalSupport
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

    @Operation(summary = "Get current user's offers", description = "Returns all offers created by the currently authenticated user")
    @GetMapping("/my")
    public List<OfferResponse> getMyOffers(Authentication authentication) {
        return offerService.getMyOffers(authentication.getName());
    }

}
