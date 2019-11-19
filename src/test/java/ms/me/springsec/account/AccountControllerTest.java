package ms.me.springsec.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ms", roles = "USER")
    public void index_anonymous_anno() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_user() throws Exception {
        mockMvc.perform(get("/").with(user("ms").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_user_not() throws Exception {
        mockMvc.perform(get("/admin").with(user("ms").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void index_admin() throws Exception {
        mockMvc.perform(get("/").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void login_success() throws Exception {
        final String username = "ms";
        final String password = "123";

        final Account account = Account.builder().username(username).password(password).role("USER").build();
        accountService.createNew(account);

        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    public void login_fail() throws Exception {
        final String username = "ms";
        final String password = "123";

        final Account account = Account.builder().username(username).password(password+"badchar").role("USER").build();
        accountService.createNew(account);

        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(unauthenticated());
    }
}