package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("SELLER")
public class Seller{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sellerId;

  @Column(name = "name")
  private String name;

  @Column(name = "store_name")
  private String storeName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @OneToOne
  @JoinColumn(name = "user_id")
  private Users user;

//  @Getter
//  @OneToMany(mappedBy = "users", cascade = {
//    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE
//  })
//  private List<Address> addressesInSeller = new ArrayList<>();

  public Seller(String storeName, String phoneNumber, String name) {
    this.storeName = storeName;
    this.phoneNumber = phoneNumber;
    this.name = name;
  }
}
