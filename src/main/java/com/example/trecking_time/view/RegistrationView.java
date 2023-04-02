package com.example.trecking_time.view;

import com.example.trecking_time.service.interfaces.UserService;
import com.example.trecking_time.utils.Converter;
import com.example.trecking_time.utils.RegistrationFormBinder;
import com.example.trecking_time.view.component.RegistrationForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Route("registration")
@AnonymousAllowed
@Component
@Scope("prototype")
public class RegistrationView extends VerticalLayout {

    public RegistrationView(UserService userService, Converter converter){
        RegistrationForm registrationForm = new RegistrationForm();
        setHorizontalComponentAlignment(Alignment.CENTER,registrationForm);

        add(registrationForm);

        RegistrationFormBinder registrationFormBinder = new RegistrationFormBinder(registrationForm, userService,converter);
        registrationFormBinder.addBindingAndValidation();
    }

}
