package sigma.training.ctp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.service.UserService;
import sigma.training.ctp.service.WalletService;

@RestController
public class WalletController {

  @Autowired
  private UserService userService;
  @Autowired
  private WalletService walletService;

  @GetMapping(path = "/my-wallet")
  public WalletRestDto getUserWallet() {
    return walletService.getWalletByUserId(userService.getCurrentUser().getId());
  }
}
