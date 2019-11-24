package com.tagsoft.registry.controller;

import com.tagsoft.registry.model.Contact;
import com.tagsoft.registry.model.Customer;
import com.tagsoft.registry.model.canada.CanadaContact;
import com.tagsoft.registry.model.dto.CustomerDataDTO;
import com.tagsoft.registry.model.us.USContact;
import com.tagsoft.registry.service.ContactService;
import com.tagsoft.registry.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

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
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    @ResponseBody public ResponseEntity<String> createNewUser(CustomerDataDTO model) {

        System.out.println("*************************************************************************** MODEL " + model);
//        ModelAndView modelAndView = new ModelAndView();
        Customer saved = customerService.findByLogin(model.getLogin());
        if (saved != null) {
            // 409 Conflict
            return new ResponseEntity<>("Sorry such login already exists", HttpStatus.CONFLICT);
        }
        Customer build = Customer.builder()
                .login(model.getLogin())
                .password(model.getPassword())
                .build();
        Contact contact = null;
        if (model.getCountry().trim().toLowerCase().equals("usa")) {
            contact = USContact.builder()
                    .name(model.getName())
                    .lastName(model.getSurname())
                    .email(model.getEmail())
                    .country(model.getCountry())
                    .states(new HashSet<>(model.getStates()))
                    .build();
            System.out.println("*************************************************************************** USACONTACT " + contact);
        } else if (model.getCountry().trim().toLowerCase().equals("canada")) {
            contact = CanadaContact.builder()
                    .name(model.getName())
                    .lastName(model.getSurname())
                    .email(model.getEmail())
                    .country(model.getCountry())
                    .province(model.getProvince())
                    .city(model.getCity())
                    .build();
            System.out.println("***************************************************************************CANADACONTACT " + contact);
        }
        System.out.println("***************************************************************************AFTERCONTACT " + contact);
        // TODO add exception handling
        contact.setCustomer(build);
        build = customerService.save(build);
        System.out.println("*************************************************************************** CUSTOMER_SAVED" + build);
        contact = contactService.save(contact);


        return new ResponseEntity<>("Customer has been registered successfully.Do you want to register someone else?",
                HttpStatus.CREATED);
    }
}
