package com.tagsoft.registry.controller;

import com.tagsoft.registry.model.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping(value = "/free-access")
    public ModelAndView letInAnonimous() {
        ModelAndView modelAndView = new ModelAndView();
        Customer guest = Customer.builder()
                .login("guest")
                .password("111")
                .id(1L)
                .build();
        modelAndView.addObject(guest);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping(value={"/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/access-denied")
    public ModelAndView setAccessDeniedView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("access-denied");
        return modelAndView;
    }
}
