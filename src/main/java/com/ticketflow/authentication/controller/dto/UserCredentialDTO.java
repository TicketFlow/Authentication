package com.ticketflow.authentication.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialDTO {

    private String type;

    private String value;

    private boolean temporary;

}
