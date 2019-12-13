package ms.me.springsec.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DashboardServiceTest {

    @Autowired
    DashboardService dashboardService;
    @Autowired
    AccountService accountService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Test
    void dashboard(){
        Account account = new Account();
        account.setRole("USER");
        account.setUsername("ms");
        account.setPassword("123");

        accountService.createNew(account);
        UserDetails userDetails = accountService.loadUserByUsername("ms");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "123");
        final Authentication authenticate = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        dashboardService.dashboard();
    }
}