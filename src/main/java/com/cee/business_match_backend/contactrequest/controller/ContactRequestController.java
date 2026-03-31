package com.cee.business_match_backend.contactrequest.controller;

import com.cee.business_match_backend.contactrequest.dto.ContactRequestResponse;
import com.cee.business_match_backend.contactrequest.dto.CreateContactRequest;
import com.cee.business_match_backend.contactrequest.dto.UpdateContactRequestStatusRequest;
import com.cee.business_match_backend.contactrequest.service.ContactRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-requests")
@RequiredArgsConstructor
@Tag(name = "Contact Requests", description = "Contact request workflow endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ContactRequestController {

    private final ContactRequestService contactRequestService;

    @Operation(summary = "Create contact request", description = "Creates a new contact request for an offer")
    @PostMapping
    public ContactRequestResponse createRequest(@Valid @RequestBody CreateContactRequest request,
                                                Authentication authentication) {
        return contactRequestService.createRequest(request, authentication.getName());
    }

    @Operation(summary = "Get sent contact requests", description = "Returns contact requests sent by the current user")
    @GetMapping("/sent")
    public List<ContactRequestResponse> getSentRequests(Authentication authentication) {
        return contactRequestService.getSentRequests(authentication.getName());
    }

    @Operation(summary = "Get received contact requests", description = "Returns contact requests received on offers owned by the current user")
    @GetMapping("/received")
    public List<ContactRequestResponse> getReceivedRequests(Authentication authentication) {
        return contactRequestService.getReceivedRequests(authentication.getName());
    }

    @Operation(summary = "Update contact request status", description = "Offer owner can ACCEPT or REJECT a contact request")
    @PutMapping("/{id}/status")
    public ContactRequestResponse updateStatus(@PathVariable Long id,
                                               @Valid @RequestBody UpdateContactRequestStatusRequest request,
                                               Authentication authentication) {
        return contactRequestService.updateRequestStatus(id, request, authentication.getName());
    }

}
