package politechnika.lodzka.qrcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import politechnika.lodzka.qrcode.model.OperationToken;
import politechnika.lodzka.qrcode.model.request.ResetPasswordRequest;
import politechnika.lodzka.qrcode.repository.TokenRepository;

import java.util.Optional;

@Controller
@RequestMapping(value = "/view")
public class ViewController {
    private final TokenRepository tokenRepository;

    public ViewController(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @ModelAttribute("resetPasswordForm")
    public ResetPasswordRequest resetPasswordRequest() {
        return new ResetPasswordRequest();
    }

    @GetMapping(value = "/success")
    public String successfulActivation() {
        return "SuccessfulActivation";
    }

    @GetMapping(value = "/failure")
    public String invalidActivation() {
        return "InvalidActivation";
    }

    @GetMapping(value = "/successPasswordChange")
    public String successfulPasswordChange() {
        return "SuccessfulPasswordChange";
    }

    @GetMapping(value = "/failurePasswordChange")
    public String invalidPasswordChange() {
        return "InvalidPasswordChange";
    }

    @GetMapping(value = "/resetPassword")
    public String displayResetPasswordPage(@RequestParam(required = false) String token,
                                           Model model) {
        Optional<OperationToken> operationToken = tokenRepository.findByToken(token);

        if (!operationToken.isPresent()) {
            model.addAttribute("error", "Could not find password reset token");
        } else {
            model.addAttribute("token", operationToken.get().getToken());
        }

        return "ResetPassword";
    }
}
