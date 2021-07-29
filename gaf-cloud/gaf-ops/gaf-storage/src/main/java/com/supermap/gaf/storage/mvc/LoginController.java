package com.supermap.gaf.storage.mvc;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping(path = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return "login.html";
    }

    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "forward:/index.html";
    }

}
