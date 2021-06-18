package sigma.training.ctp.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "currency")
@Data
@NoArgsConstructor
public class CurrencyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "basic")
  private boolean basic;

  @Column(name = "bankCurrency")
  private boolean bankCurrency;

  @Column(name = "name")
  private String name;

  @Column(name = "acronym")
  private String acronym;


}
