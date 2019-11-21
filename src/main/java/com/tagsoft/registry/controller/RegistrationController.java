package com.tagsoft.registry.controller;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.service.ContactService;
import com.tagsoft.registry.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class RegistrationController {

    private final CustomerService customerService;
    private final ContactService contactService;

    public RegistrationController(CustomerService customerService, ContactService contactService) {
        this.customerService = customerService;
        this.contactService = contactService;
    }

    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        // create a models attached to page
        modelAndView.addObject("customer", new Customer());
        modelAndView.addObject("contact", new Contact());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid @ModelAttribute(value = "customer") Customer customer,
                                      @Valid @ModelAttribute(value = "contact") Contact contact,
                                      BindingResult bindingResult) {
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
            Customer newCustomer = customerService.save(customer);
            contact.setCustomer(newCustomer);
            String crutch = contact.getCrutch();
            String[] s = crutch.split(" ");
            contact.setStates(Arrays.asList(s));
            contactService.save(contact);
            modelAndView.addObject("successMessage", "Customer has been registered successfully. " +
                    "Do you want to register someone else?");
            modelAndView.addObject("customer", new Customer());
            modelAndView.addObject("contact", new Contact());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }
}
