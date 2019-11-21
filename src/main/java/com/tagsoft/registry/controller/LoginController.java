package com.tagsoft.registry.controller;

import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private final CustomerService customerService;

    @Autowired
    public LoginController(CustomerService customerService) {
        this.customerService = customerService;
    }

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

    @GetMapping(value="/index")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        // NB the way to get access to user data
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByLogin(auth.getName());
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/access-denied")
    public ModelAndView setAccessDeniedView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("access-denied");
        return modelAndView;
    }
}
