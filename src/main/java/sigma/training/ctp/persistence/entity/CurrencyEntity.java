package sigma.training.ctp.persistence.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

  public void setId(Long id) {
    if (id == null) {
      throw new NullPointerException("The id value hasn't been initialized");
    }

    this.id = id;
  }

  public void setName(String name) {
    if (name == null) {
      throw new NullPointerException("The currency name value has no reference to an object typed as String");
    }
    if (name.isEmpty()) {
      throw new IllegalArgumentException("The currency name is empty");
    }

    this.name = name;
  }

  public void setAcronym(String acronym) {
    if (acronym == null) {
      throw new NullPointerException("The acronym value has no reference to an object typed as String");
    }
    if (acronym.isEmpty()) {
      throw new IllegalArgumentException("The acronym value is empty");
    }

    this.acronym = acronym;
  }
}
