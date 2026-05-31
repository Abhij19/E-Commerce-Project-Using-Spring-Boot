package ecom.commerce.user.dto;

import lombok.Data;

@Data
public class AddressDto {

    private String street;
    private String state;
    private String city;
    private String country;
    private String zipcode;
}
