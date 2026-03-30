package com.cee.business_match_backend.contactrequest.dto;

import com.cee.business_match_backend.contactrequest.model.ContactRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContactRequestStatusRequest {

    @NotNull
    private ContactRequestStatus status;

}
