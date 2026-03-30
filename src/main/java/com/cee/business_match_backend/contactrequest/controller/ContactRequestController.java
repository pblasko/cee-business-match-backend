package com.cee.business_match_backend.contactrequest.controller;

import com.cee.business_match_backend.contactrequest.dto.ContactRequestResponse;
import com.cee.business_match_backend.contactrequest.dto.CreateContactRequest;
import com.cee.business_match_backend.contactrequest.service.ContactRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-requests")
@RequiredArgsConstructor
public class ContactRequestController {

    private final ContactRequestService contactRequestService;

    @PostMapping
    public ContactRequestResponse createRequest(@Valid @RequestBody CreateContactRequest request,
                                                Authentication authentication) {
        return contactRequestService.createRequest(request, authentication.getName());
    }

    @GetMapping("/sent")
    public List<ContactRequestResponse> getSentRequests(Authentication authentication) {
        return contactRequestService.getSentRequests(authentication.getName());
    }

    @GetMapping("/received")
    public List<ContactRequestResponse> getReceivedRequests(Authentication authentication) {
        return contactRequestService.getReceivedRequests(authentication.getName());
    }

}
