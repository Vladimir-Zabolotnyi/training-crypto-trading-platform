package sigma.training.ctp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.model.UserWalletBalanceViewModel;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.service.UserWalletService;

@RestController
public class UserWalletController {

    @Autowired
    private UserWalletService service;

    @GetMapping(path = "/my-wallet")
    public UserWalletBalanceViewModel getUserWallet() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return service.getWalletByUserId(user.getId());
    }
}
