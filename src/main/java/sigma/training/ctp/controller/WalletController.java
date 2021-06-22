package sigma.training.ctp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.WalletNotFoundException;
import sigma.training.ctp.service.AuditTrailService;
import sigma.training.ctp.service.UserService;
import sigma.training.ctp.service.WalletService;

import java.util.List;


@RestController
@RequestMapping
@Tag(name = "Wallet Controller", description = "Allows to get wallet of the user")
public class WalletController {

  @Autowired
  private UserService userService;
  @Autowired
  private WalletService walletService;
  @Autowired
  private AuditTrailService auditTrailService;

  @Operation(
    summary = "Returns the user's wallet with a defined currency",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "A model that contains the information about the wallet with defined currency",
        content = @Content(schema = @Schema(implementation = WalletRestDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Wallet was not found",
        content = @Content(mediaType = "text/plain"))
    }
  )
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/my-wallets")
  public @ResponseBody WalletRestDto getUserWallet(
    @RequestParam(name = "currencyName")
    @Parameter(in = ParameterIn.QUERY, description = "name of the currency", example = "Bitcoin") String currencyName) throws WalletNotFoundException {
    WalletRestDto wallet = walletService.getWalletByUserIdAndCurrencyName(userService.getCurrentUser().getId(), currencyName);
    auditTrailService.postAuditTrail("User read the wallet (id: " + wallet.getId() + ")");
    return wallet;
  }

  @Operation(
    summary = "Returns the user's wallets",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "A model that contains the information about the wallet ",
        content = @Content(schema = @Schema(implementation = WalletRestDto.class))
      )
    }
  )
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/my-wallets/")
  public @ResponseBody List<WalletRestDto> getAllUserWallets() {
    List<WalletRestDto> walletList = walletService.getAllWalletsByUserId(userService.getCurrentUser().getId());
    auditTrailService.postAuditTrail("User read list of own wallets");
    return walletList;
  }
}
