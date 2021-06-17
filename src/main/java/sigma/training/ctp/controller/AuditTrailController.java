package sigma.training.ctp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.AuditTrailDto;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.service.AuditTrailService;

import java.util.List;


@RestController
@RequestMapping("/audit-trails")
@Tag(name = "Audit trails controller", description = "Allows to see get audit-trails")
public class AuditTrailController {

  @Autowired
  AuditTrailService auditTrailService;


  @Operation(summary = "Get all audit trails", description = "Allows to obtain information about all actions on the platform")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "audit-trails are obtained",
      content = @Content(mediaType = "application/json", array = @ArraySchema(
        schema = @Schema(implementation = AuditTrail.class)))),
    @ApiResponse(responseCode = "404", description = "No audit-trails were found",
      content = @Content(mediaType = "text/plain")),
    @ApiResponse(responseCode = "403", description = "Not allowed",
      content = @Content(mediaType = "text/plain"))
  })
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public @ResponseBody List<AuditTrailDto> getAllOrders() {
    return auditTrailService.getAllAuditTrails();
  }
}
