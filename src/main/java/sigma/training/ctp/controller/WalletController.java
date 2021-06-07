package sigma.training.ctp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.service.UserService;
import sigma.training.ctp.service.WalletService;

@Schema(
  name = "Wallet controller",
  description = "An access point to the wallet data",
  anyOf = WalletController.class
)
@RestController
public class WalletController {

  @Autowired
  private UserService userService;
  @Autowired
  private WalletService walletService;

  @Operation(
    summary = "Returns the user's wallet balance",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "A model that contains the information about the money and cryptocurrency balance",
        content = @Content(schema = @Schema(anyOf = WalletRestDto.class))
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized"
      )
    }
  )
  @GetMapping(path = "/my-wallet")
  public WalletRestDto getUserWallet() {
    return walletService.getWalletByUserId(userService.getCurrentUser().getId());
  }
}
