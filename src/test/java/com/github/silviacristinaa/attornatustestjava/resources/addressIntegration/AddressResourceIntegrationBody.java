package com.github.silviacristinaa.attornatustestjava.resources.addressIntegration;

import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressIsMainRequestDto;
import com.github.silviacristinaa.attornatustestjava.dtos.requests.AddressRequestDto;

public class AddressResourceIntegrationBody {

    public static AddressRequestDto addressBadRequest() {
        return new AddressRequestDto(null, null,
                1l, "test", true);
    }

    public static AddressRequestDto addressCreateSuccess() {
        return new AddressRequestDto("Test", "88888888", 1l, "test", true);
    }

    public static AddressIsMainRequestDto addressUpdateFieldIsMain(boolean isMain) {
        return new AddressIsMainRequestDto(isMain);
    }
}