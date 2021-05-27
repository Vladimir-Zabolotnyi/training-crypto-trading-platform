package sigma.training.ctp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.persistence.entity.UserWalletEntity;
import sigma.training.ctp.service.UserWalletService;

import java.util.List;

@RestController
public class UserWalletController {

    @Autowired
    private UserWalletService service;

    @GetMapping(path = "/my-wallet{id}")
    public List<UserWalletEntity> getUserWallets(
            @PathVariable(value = "id") Long id
    ) {
        return service.getAllWalletsByUserId(id);
    }
}
