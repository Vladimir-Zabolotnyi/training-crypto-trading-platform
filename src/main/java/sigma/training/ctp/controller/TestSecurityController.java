package sigma.training.ctp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.service.UserService;


@RestController
public class TestSecurityController {

@Autowired
UserService userService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody String getAuthor() {
        return "Test passed";
    }
}