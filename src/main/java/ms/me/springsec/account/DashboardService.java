package ms.me.springsec.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    public void dashboard() {
        final Account account = AccountContext.getAccount();
        log.info("username: {}", account.getUsername());
    }
}
