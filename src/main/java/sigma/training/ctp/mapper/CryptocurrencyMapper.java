package sigma.training.ctp.mapper;

import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.CryptocurrencyRestDto;
import sigma.training.ctp.persistence.entity.CurrencyEntity;

@Component
public class CryptocurrencyMapper implements Mapper<CurrencyEntity, CryptocurrencyRestDto> {

  @Override
  public CryptocurrencyRestDto toRestDto(CurrencyEntity entity) {
    return new CryptocurrencyRestDto(
      entity.getId(),
      entity.getName(),
      entity.getAcronym()
    );
  }

  @Override
  public CurrencyEntity toEntity(CryptocurrencyRestDto restDto) {
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
