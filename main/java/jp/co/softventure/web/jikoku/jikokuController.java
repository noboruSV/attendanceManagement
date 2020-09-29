package jp.co.softventure.web.jikoku;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class jikokuController {

    @ModelAttribute
    public jikokuForm setForm() {
        return new jikokuForm();
    }

    @RequestMapping("/jikokuinput")
    public String input() {
        return "jikoku/jikokuInput";
    }
}