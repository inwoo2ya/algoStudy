package com.example.algoStudy2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class maincontroller {
    @GetMapping("/")
    public String main(){
        return "main";
    }
}


