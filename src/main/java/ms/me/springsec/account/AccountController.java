package ms.me.springsec.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/account/{role}/{username}/{password}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@ModelAttribute Account account){
        return accountService.createNew(account);
    }
}
