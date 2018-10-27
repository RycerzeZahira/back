package politechnika.lodzka.qrcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/view")
public class ViewController {

    @GetMapping(value = "/success")
    public String successfulActivation() {
        return "SuccessfulActivation";
    }

    @GetMapping(value = "/failure")
    public String invalidActivation() {
        return "InvalidActivation";
    }
}
