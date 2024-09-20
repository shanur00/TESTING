package com.ecommerce.project.request;

import com.ecommerce.project.payload.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerSignUpRequest {
  private String username;
  private String name;
  private String storeName;
  private String phoneNumber;
  private List<AddressDTO> addressDTOS = new ArrayList<>();
}
