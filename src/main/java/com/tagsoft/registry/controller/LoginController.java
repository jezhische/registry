package com.tagsoft.registry.controller;

import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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


    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Customer customer = new Customer();
        // create a model attached to page
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid @ModelAttribute(value = "customer") Customer customer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Customer saved = customerService.findByLogin(customer.getLogin());
        if (saved != null) {
            bindingResult
                    .rejectValue("login", "error.customer",
                            "There is already a customer registered with the login provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            customerService.save(customer);
            modelAndView.addObject("successMessage", "Customer has been registered successfully");
            modelAndView.addObject("customer", new Customer());
            modelAndView.setViewName("registration");

        }
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
