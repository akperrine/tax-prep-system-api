package com.skillstorm.taxprepsystemapi.dtos.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInDTO {
    private String email;
    private String password;
}
