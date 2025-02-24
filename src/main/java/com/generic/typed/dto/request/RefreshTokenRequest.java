package com.generic.typed.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotEmpty
    String refreshToken;
}
