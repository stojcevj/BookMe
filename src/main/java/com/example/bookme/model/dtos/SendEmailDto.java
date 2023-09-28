package com.example.bookme.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SendEmailDto {
  private String from;
  private String subject;
  private String text;
}
