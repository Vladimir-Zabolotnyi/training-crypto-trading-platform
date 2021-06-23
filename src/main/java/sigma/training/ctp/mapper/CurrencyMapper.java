package sigma.training.ctp.mapper;

import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.CurrencyRestDto;
import sigma.training.ctp.persistence.entity.CurrencyEntity;

@Component
public class CurrencyMapper implements Mapper<CurrencyEntity, CurrencyRestDto> {

  @Override
  public CurrencyRestDto toRestDto(CurrencyEntity entity) {
    return new CurrencyRestDto(
      entity.getId(),
      entity.getName(),
      entity.getAcronym()
    );
  }

  @Override
  public CurrencyEntity toEntity(CurrencyRestDto restDto) {
    CurrencyEntity currencyEntity = new CurrencyEntity();

    if (restDto == null) {
      throw new NullPointerException("The proposed DTO-object has no reference to an object");
    }

    currencyEntity.setId(restDto.getId());
    currencyEntity.setName(restDto.getName());
    currencyEntity.setAcronym(restDto.getAcronym());

    return currencyEntity;
  }
}
