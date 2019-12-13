package ms.me.springsec.form;

import lombok.RequiredArgsConstructor;
import ms.me.springsec.account.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SampleController {

    private final DashboardService dashboardService;
    private final AccountRepository accountRepository;

    @GetMapping("/")
//    public String index(Model model, Principal principal){
//    public String index(Model model, @AuthenticationPrincipal UserAccount userAccount){
    public String index(Model model, @CurrentUser Account userAccount){

        if(userAccount == null){
            model.addAttribute("message", "Hello");
        }else{
            model.addAttribute("message", "Hello, " + userAccount.getUsername());
        }
        return "index";
    }

    @GetMapping("/info")
    public String info(Model model){
        model.addAttribute("message", "info");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("message", "Hello " + principal.getName());
        model.addAttribute("message", "hello" + principal.getName());
        AccountContext.setAccount(accountRepository.findByUsername(principal.getName()).orElseThrow());
        dashboardService.dashboard();

        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal){
        model.addAttribute("message", "Hello Admin, " + principal.getName());
        return "index";
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal){
        model.addAttribute("message", "Hello User, " + principal.getName());
        return "user";
    }
}
