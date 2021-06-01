package sigma.training.ctp.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
CREATED("created"),CANCELLED("cancelled"),FULFILLED("fulfilled");

private final String status;


}
