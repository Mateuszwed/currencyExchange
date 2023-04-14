package com.mateuszwed.currencyExchange.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rates")
public class RateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String currency;
    String code;
    BigDecimal mid;
}