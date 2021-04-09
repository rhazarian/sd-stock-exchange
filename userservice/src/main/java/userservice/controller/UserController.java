package userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import userservice.model.User;
import userservice.service.UserService;

@Controller
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping(value = "/add-user")
    public ResponseEntity<Long> addUser() {
        final var user = new User();
        return ResponseEntity.ok(service.addUser(user));
    }

    @RequestMapping(value = "/add-balance")
    public ResponseEntity<Boolean> addStocks(@RequestParam("user-id") long userId, @RequestParam("amount") long amount) {
        service.addBalance(userId, amount);
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/buy-stocks", method = RequestMethod.POST)
    public ResponseEntity<Boolean> buyStocks(@RequestParam("user-id") long userId, @RequestParam("company-id") long companyId, @RequestParam("amount") long amount) {
        if (service.buyStocks(userId, companyId, amount)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @RequestMapping(value = "/sell-stocks", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sellStocks(@RequestParam("user-id") long userId, @RequestParam("company-id") long companyId, @RequestParam("amount") long amount) {
        if (service.sellStocks(userId, companyId, amount)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
