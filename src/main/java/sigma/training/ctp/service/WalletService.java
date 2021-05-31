package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;
import sigma.training.ctp.dto.WalletBalanceRestDto;

@Service
public class WalletService {

    @Autowired
    private WalletRepository repository;

    public WalletBalanceRestDto getWalletByUserId(Long id) {
        WalletEntity wallet = repository.findUserWalletEntityByUserId(id);

        return new WalletBalanceRestDto(
                wallet.getMoneyBalance(),
                wallet.getCryptocurrencyBalance()
        );
    }
}