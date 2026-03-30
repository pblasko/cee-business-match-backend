package com.cee.business_match_backend.contactrequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContactRequest {

    @NotNull
    private Long offerId;

    @NotBlank
    private String message;

}
