package hu.martin.chatter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {

    @GetMapping
    public String getReact() {
        return "forward:/";
    }
}
