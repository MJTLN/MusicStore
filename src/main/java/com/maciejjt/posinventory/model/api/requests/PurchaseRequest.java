package com.maciejjt.posinventory.model.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {
    @NotNull(message = "Product ids cannot be null")
    @Size(min =  1, message = "Please select at least one product")
    private List<Long> productIds;
    @NotNull(message = "Shipper cannot be null")
    private String shipper;
    @NotNull(message = "Country cannot be null")
    private String country;
    @NotNull(message = "City cannot be null")
    private String city;
    @NotNull(message = "Street cannot be null")
    private String street;
    @NotNull(message = "House cannot be null")
    private String house;
    @NotNull(message = "Phone cannot be null")
    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", message = "Incorrect phone number format")
    private String phone;
    private String apartment;
    @Email(message = "Please supply a valid email address")
    private String mail;
}
