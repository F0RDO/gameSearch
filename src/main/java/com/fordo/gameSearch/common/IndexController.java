package com.fordo.gameSearch.common;

import org.springframework.web.bind.annotation.GetMapping;

public class IndexController {
    @GetMapping("/")
    public String moveIndex(){
        return "index";
    }
}
