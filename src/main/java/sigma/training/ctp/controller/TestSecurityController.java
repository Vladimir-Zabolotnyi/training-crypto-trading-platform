package sigma.training.ctp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.UserRestDto;
import sigma.training.ctp.mapper.UserMapper;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.service.UserService;


@RestController
@RequestMapping("/test")
@Hidden
@Tag(name = "Security Controller Test", description = "Allows to check Basic Auth")
public class TestSecurityController {

  @Autowired
  UserService userService;

  @Autowired
  UserMapper userMapper;

  @GetMapping
  @Operation(summary = "Security test", description = "Check login and password")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successfully authenticated",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRestDto.class))),
    @ApiResponse(responseCode = "400", description = "Bad request",
                 content = @Content)
  })
  public @ResponseBody
  UserRestDto securityTest() {
    UserEntity user = (UserEntity) SecurityContextHolder.getContext()
                                                        .getAuthentication()
                                                        .getPrincipal();

    return userMapper.toRestDto(user);
  }
}
