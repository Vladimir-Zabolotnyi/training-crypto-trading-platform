package sigma.training.ctp.mapper;


import lombok.Data;
import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;

import java.util.List;
import java.util.stream.Collectors;


@Component
@Data
public class WalletMapper implements Mapper<WalletEntity, WalletRestDto> {

  @Override
  public WalletRestDto toRestDto(WalletEntity wallet) {
    return new WalletRestDto(wallet.getId(), wallet.getCurrency().getName(), wallet.getCurrency().getAcronym(), wallet.getAmount());
  }

  public List<WalletRestDto> toRestDto(List<WalletEntity> walletList) {
    return walletList.stream().map(
      walletEntity -> toRestDto(walletEntity)).collect(Collectors.toList());
  }

  @Override
  public WalletEntity toEntity(WalletRestDto walletDto) {
    UserEntity user = new UserEntity();
    CurrencyEntity currency = new CurrencyEntity();
    currency.setAcronym(walletDto.getCurrencyAcronym());
    currency.setName(walletDto.getCurrencyName());
    return new WalletEntity(walletDto.getId(), user, currency, walletDto.getAmount());
  }

}
