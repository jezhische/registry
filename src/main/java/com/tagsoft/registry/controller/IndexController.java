package com.tagsoft.registry.controller;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.dto.CustomerDataDTO;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.service.ContactService;
import com.tagsoft.registry.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
                .country(contact.getCountry())
                .name(contact.getName())
                .surname(contact.getLastName())
                .email(contact.getEmail())
                .build();
        if (customerData.getCountry().equalsIgnoreCase("USA"))
            customerData.setStates(((USContact) contact).getStates());
        else if (customerData.getCountry().equalsIgnoreCase("Canada")) {
            customerData.setProvince(((CanadaContact) contact).getProvince());
            customerData.setCity(((CanadaContact) contact).getCity());
        }
        return ResponseEntity.ok().body(customerData);
    }

    @PutMapping("/customer-data")
    public @ResponseBody
    ResponseEntity<String> updateCustomer(CustomerDataDTO edited) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByLogin(auth.getName());
        Contact contact = contactService.findById(customer.getId()).orElse(new Contact());
        contact.setName(edited.getName());
        contact.setLastName(edited.getSurname());
// TODO: если выбирается другая страна, нужно полностью менять тип Contact. Нужно
//  либо запретить менять страну, либо сделать обработчик. Я пока что запретил.
//        contact.setCountry(edited.getCountry());
        contact.setEmail(edited.getEmail());
        if (contact.getCountry().equalsIgnoreCase("usa")) {
            ((USContact) contact).setStates(edited.getStates());
        } else {
            ((CanadaContact) contact).setProvince(edited.getProvince());
            ((CanadaContact) contact).setCity(edited.getCity());
        }
        contactService.update(contact);
        return ResponseEntity.ok("Your profile edited successful");
    }

    @DeleteMapping("/customer-data")
    public @ResponseBody
    ResponseEntity<String> deleteAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByLogin(auth.getName());
        customerService.deleteByLogin(customer.getLogin());
        return ResponseEntity.ok("Your account deleted successful");
    }
}
