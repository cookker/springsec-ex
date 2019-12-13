package ms.me.springsec.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

//        return optionalAccount.map(a -> User.builder()
//                                    .username(a.getUsername())
//                                    .password(a.getPassword())
//                                    .roles(a.getRole())
//                                    .build())
//                                .orElseThrow(() -> new UsernameNotFoundException(username));

        final Account account = optionalAccount.orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserAccount(account);
    }

    public Account createNew(Account account) {
        account.encodePassword(passwordEncoder);
        return this.accountRepository.save(account);
    }
}
