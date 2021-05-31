package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;
import sigma.training.ctp.rest_dto.WalletBalanceRestDTO;

@Service
public class WalletService {

    @Autowired
    private WalletRepository repository;

    public WalletBalanceRestDTO getWalletByUserId(Long id) {
        WalletEntity wallet = repository.findUserWalletEntityByUserId(id);

        return new WalletBalanceRestDTO(
                wallet.getMoneyBalance(),
                wallet.getCryptocurrencyBalance()
        );
    }
}