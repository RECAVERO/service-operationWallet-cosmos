package com.nttdata.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationWalletDto {
  private Long nroDocument;
  private int typeDocument;
  private String numberCard;
  private String numberAccount;
  private String numberTelephone;
  private double amount;
  private String registrationDate;
  private int typeOperation;
  private double sumAmount;
  private String created_datetime;
  private String updated_datetime;
  private String active;
}
