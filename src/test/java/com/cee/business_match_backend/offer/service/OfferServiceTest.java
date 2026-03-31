package com.cee.business_match_backend.offer.service;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.auth.repository.UserRepository;
import com.cee.business_match_backend.common.exception.BusinessException;
import com.cee.business_match_backend.offer.dto.CreateOfferRequest;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OfferService offerService;

    @Test
    void shouldThrowExceptionWhenLawFirmTriesToCreateOffer() {
        User lawFirmUser = User.builder()
                .id(1L)
                .email("law@test.com")
                .role(Role.LAW_FIRM)
                .build();

        CreateOfferRequest request = new CreateOfferRequest();
        request.setTitle("Invalid law firm offer");
        request.setDescription("Should fail");
        request.setCategory(OfferCategory.LEGAL_SERVICE);
        request.setTargetRole(Role.EXPORTER);
        request.setCountry("Hungary");
        request.setNeedsLegalSupport(false);

        when(userRepository.findByEmail("law@test.com")).thenReturn(Optional.of(lawFirmUser));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> offerService.createOffer(request, "law@test.com")
        );

        assertEquals("Law firms cannot create offers", exception.getMessage());
    }

}
