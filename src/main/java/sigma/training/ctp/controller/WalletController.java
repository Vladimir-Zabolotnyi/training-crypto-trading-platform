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
  name = "Wallet Controller",
  description = "An endpoint to the wallet service object",
  anyOf = WalletController.class
)
@RestController
public class WalletController {

  @Autowired
  private UserService userService;
  @Autowired
  private WalletService walletService;

  @Operation(
    method = "GET",
    summary = "Returns the user wallet and puts it into the DTO-object called WalletRestDto",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "A JSON-object that contains the information about the money and cryptocurrency balance",
        content = @Content(mediaType = "application/json", schema = @Schema(anyOf = WalletRestDto.class))
      ),
      @ApiResponse(
        responseCode = "401",
        description = "The proposed user hasn't been authorized"
      )
    }
  )
  @GetMapping(path = "/my-wallet")
  public WalletRestDto getUserWallet() {
    return walletService.getWalletByUserId(userService.getCurrentUser().getId());
  }
}
