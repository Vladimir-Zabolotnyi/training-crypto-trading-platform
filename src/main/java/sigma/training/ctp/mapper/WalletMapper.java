package sigma.training.ctp.mapper;


import lombok.Data;
import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;


@Component
@Data
public class WalletMapper {


  public WalletRestDto toRestDto(WalletEntity wallet) {

    return new WalletRestDto(wallet.getCurrency().getName(),wallet.getCurrency().getAcronym(),wallet.getAmount());
  }

  public WalletEntity toEntity(WalletRestDto walletDto) {
    UserEntity user= new UserEntity();
    CurrencyEntity currency = new CurrencyEntity();
    currency.setAcronym(walletDto.getCurrencyAcronym());
    currency.setName(walletDto.getCurrencyName());
    return new WalletEntity(user,currency,walletDto.getAmount());
  }

}
