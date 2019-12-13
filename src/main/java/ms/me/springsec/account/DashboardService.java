package ms.me.springsec.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    @Secured("ROLE_ADMIN")
//    @RolesAllowed("ROLE_USER")
//    @PreAuthorize("hasRole('USER')")
    public void dashboard() {
        final Account account = AccountContext.getAccount();
//        log.info("username: {}", account.getUsername());
    }
}
