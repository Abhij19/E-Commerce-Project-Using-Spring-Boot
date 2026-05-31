package ecom.commerce.user.model;

import lombok.Data;

@Data
public class Address {
    private long id;
    private String street;
    private String state;
    private String city;
    private String country;
    private String zipcode;

}
