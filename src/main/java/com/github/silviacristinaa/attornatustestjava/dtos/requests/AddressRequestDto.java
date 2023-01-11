package com.github.silviacristinaa.attornatustestjava.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AddressRequestDto {

    @NotBlank
    private String publicPlace;
    @NotBlank
    private String zipCode;
    @NotNull
    private Long number;
    @NotBlank
    private String city;
    private boolean isMain;
}