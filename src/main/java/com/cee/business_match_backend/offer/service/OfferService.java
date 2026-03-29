package com.cee.business_match_backend.offer.service;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.auth.repository.UserRepository;
import com.cee.business_match_backend.common.exception.BusinessException;
import com.cee.business_match_backend.offer.dto.CreateOfferRequest;
import com.cee.business_match_backend.offer.dto.OfferResponse;
import com.cee.business_match_backend.offer.model.Offer;
import com.cee.business_match_backend.offer.model.OfferStatus;
import com.cee.business_match_backend.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public OfferResponse createOffer(CreateOfferRequest request, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        validateOfferCreation(creator, request);

        Offer offer = Offer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .targetRole(request.getTargetRole())
                .country(request.getCountry())
                .needsLegalSupport(request.isNeedsLegalSupport())
                .status(OfferStatus.OPEN)
                .creator(creator)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(offerRepository.save(offer));
    }

    public List<OfferResponse> getAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<OfferResponse> getMyOffers(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return offerRepository.findByCreatorId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateOfferCreation(User creator, CreateOfferRequest request) {
        Role creatorRole = creator.getRole();
        Role targetRole = request.getTargetRole();

        if (creatorRole == Role.LAW_FIRM) {
            throw new BusinessException("Law firms cannot create offers");
        }

        if (creatorRole == Role.EXPORTER && targetRole != Role.INVESTOR) {
            throw new BusinessException("Exporter offers must target investors");
        }

        if (creatorRole == Role.INVESTOR && targetRole != Role.EXPORTER) {
            throw new BusinessException("Investor offers must target exporters");
        }

        if (targetRole == Role.LAW_FIRM) {
            throw new BusinessException("Law firm cannot be the primary target role");
        }
    }

    private OfferResponse mapToResponse(Offer offer) {
        return OfferResponse.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .description(offer.getDescription())
                .category(offer.getCategory())
                .targetRole(offer.getTargetRole())
                .country(offer.getCountry())
                .needsLegalSupport(offer.isNeedsLegalSupport())
                .status(offer.getStatus())
                .creatorId(offer.getCreator().getId())
                .creatorEmail(offer.getCreator().getEmail())
                .createdAt(offer.getCreatedAt())
                .build();
    }

}
