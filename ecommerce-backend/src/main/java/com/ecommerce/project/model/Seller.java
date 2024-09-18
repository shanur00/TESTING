package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("SELLER")
public class Seller extends Users{

  @Column(name = "name")
  private String name;

  @Column(name = "store_name")
  private String storeName;

  @Column(name = "phone_number")
  private String phoneNumber;

  public Seller(String username, String email, String password, String storeName, String phoneNumber, String name) {
    super(username, email, password);
    this.storeName = storeName;
    this.phoneNumber = phoneNumber;
    this.name = name;
  }
}
