package sigma.training.ctp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.service.WalletService;

@RestController
public class WalletController {

    @Autowired
    private WalletService service;

    @GetMapping(path = "/my-wallet")
    @ResponseBody
    public WalletRestDto getUserWallet(
            @Value("${bankcurrency.name}") String bankCurrencyName,
            @Value("${cryptocurrency.name}") String cryptocurrencyName,
            @Value("${cryptocurrency.sign}") String cryptocurrencySign
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        WalletRestDto walletRestDto = service.getWalletByUserId(user.getId());

        String moneyBalance = bankCurrencyName
                .concat(": ")
                .concat(walletRestDto.getMoneyBalance());
        String cryptocurrencyBalance = cryptocurrencyName
                .concat(": ")
                .concat(walletRestDto.getCryptocurrencyBalance())
                .concat(cryptocurrencySign);

        return new WalletRestDto(moneyBalance, cryptocurrencyBalance);
    }
}
