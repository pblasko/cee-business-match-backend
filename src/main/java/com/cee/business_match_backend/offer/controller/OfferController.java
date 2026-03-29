package com.cee.business_match_backend.offer.controller;

import com.cee.business_match_backend.offer.dto.CreateOfferRequest;
import com.cee.business_match_backend.offer.dto.OfferResponse;
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
    public List<OfferResponse> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/my")
    public List<OfferResponse> getMyOffers(Authentication authentication) {
        return offerService.getMyOffers(authentication.getName());
    }

}
