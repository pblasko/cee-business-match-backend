package com.cee.business_match_backend.offer.service;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.auth.repository.UserRepository;
import com.cee.business_match_backend.common.dto.PagedResponse;
import com.cee.business_match_backend.common.exception.BusinessException;
import com.cee.business_match_backend.offer.dto.CreateOfferRequest;
import com.cee.business_match_backend.offer.dto.OfferFilterRequest;
import com.cee.business_match_backend.offer.dto.OfferResponse;
import com.cee.business_match_backend.offer.model.Offer;
import com.cee.business_match_backend.offer.model.OfferStatus;
import com.cee.business_match_backend.offer.repository.OfferRepository;
import com.cee.business_match_backend.offer.specification.OfferSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OfferService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("createdAt", "title", "country", "status");

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

    public PagedResponse<OfferResponse> getOffers(OfferFilterRequest filter,
                                                  int page,
                                                  int size,
                                                  String sortBy,
                                                  String sortDir) {

        validatePaging(page, size);
        validateSortField(sortBy);

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Offer> specification = Specification
                .where(OfferSpecification.hasCategory(filter.getCategory()))
                .and(OfferSpecification.hasCountry(filter.getCountry()))
                .and(OfferSpecification.hasTargetRole(filter.getTargetRole()))
                .and(OfferSpecification.hasStatus(filter.getStatus()))
                .and(OfferSpecification.hasNeedsLegalSupport(filter.getNeedsLegalSupport()));

        Page<Offer> offerPage = offerRepository.findAll(specification, pageable);

        List<OfferResponse> content = offerPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PagedResponse.<OfferResponse>builder()
                .content(content)
                .page(offerPage.getNumber())
                .size(offerPage.getSize())
                .totalElements(offerPage.getTotalElements())
                .totalPages(offerPage.getTotalPages())
                .last(offerPage.isLast())
                .build();
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

    private void validatePaging(int page, int size) {
        if (page < 0) {
            throw new BusinessException("Page index must be zero or greater");
        }

        if (size < 1 || size > 100) {
            throw new BusinessException("Page size must be between 1 and 100");
        }
    }

    private void validateSortField(String sortBy) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new BusinessException("Invalid sort field: " + sortBy);
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
