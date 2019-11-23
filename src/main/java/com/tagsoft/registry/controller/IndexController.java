package com.tagsoft.registry.controller;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.model.dto.CustomerDataDTO;
import com.tagsoft.registry.service.ContactService;
import com.tagsoft.registry.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/index")
public class IndexController {
    private final CustomerService customerService;
    private final ContactService contactService;

    public IndexController(CustomerService customerService, ContactService contactService) {
        this.customerService = customerService;
        this.contactService = contactService;
    }

    @GetMapping
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        // NB the way to get access to user data
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByLogin(auth.getName());
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /**
     * Since I get Customer from authentication, it means only owner can view or edit his profile.
     * I.e. it means well authorized request.
     * @return
     */
    @GetMapping(value = "/customer-data")
    public @ResponseBody
    ResponseEntity<CustomerDataDTO> getCustomerData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByLogin(auth.getName());
        Contact contact = contactService.findById(customer.getId()).orElseGet(Contact::new);
        CustomerDataDTO customerData = CustomerDataDTO.builder()
                .country(customer.getCountry())
                .name(contact.getName())
                .surname(contact.getLastName())
                .email(contact.getEmail())
                .build();
        if (customerData.getCountry().equalsIgnoreCase("USA"))
            customerData.setStates(contact.getStates());
        else if (customerData.getCountry().equalsIgnoreCase("Canada")) {
            customerData.setProvince(contact.getProvince());
            customerData.setCity(contact.getCity());
        }
        return ResponseEntity.ok().body(customerData);
    }
}
