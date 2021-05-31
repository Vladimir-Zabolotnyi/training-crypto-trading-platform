package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;
import sigma.training.ctp.dto.WalletRestDto;

@Service
public class WalletService {

    @Autowired
    private WalletRepository repository;

    public WalletRestDto getWalletByUserId(Long id) {
        WalletEntity wallet = repository.findWalletEntityByUserId(id);

        return new WalletRestDto(
                wallet.getMoneyBalance(),
                wallet.getCryptocurrencyBalance()
        );
    }
}