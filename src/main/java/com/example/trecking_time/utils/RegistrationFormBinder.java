package com.example.trecking_time.utils;

import com.example.trecking_time.entity.User;
import com.example.trecking_time.entity.dto.UserDto;
import com.example.trecking_time.service.interfaces.UserService;
import com.example.trecking_time.view.component.RegistrationForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class RegistrationFormBinder {
    private final Converter converter;
    private final UserService userService;
    private final RegistrationForm registrationForm;
    private boolean enablePasswordValidation;

    public RegistrationFormBinder(RegistrationForm registrationForm,UserService userService, Converter converter) {
        this.registrationForm = registrationForm;
        this.userService = userService;
        this.converter = converter;
    }

    public void addBindingAndValidation() {
        BeanValidationBinder<UserDto> binder = new BeanValidationBinder<>(UserDto.class);
        binder.bindInstanceFields(registrationForm);

        binder.forField(registrationForm.getPasswordField()).withValidator((e,context)-> passwordValidator(e));

        registrationForm.getPasswordConfirmField().addValueChangeListener(e -> {
            enablePasswordValidation = true;
            binder.validate();
        });

        binder.setStatusLabel(registrationForm.getErrorMessageField());

        registrationForm.getSubmitButton().addClickListener(event -> {
            try {
                UserDto userBean = new UserDto();
                binder.writeBean(userBean);

                User user = converter.userToEntity(userBean);
                user.setPassword(registrationForm.getPasswordField().getValue());
                userService.createUser(user);

                showSuccess(userBean);
                UI.getCurrent().navigate("login");
//                event.getSource().getUI().ifPresent(ui ->ui.navigate("login"));
            }catch (ValidationException exception){
                exception.printStackTrace();
            }
        });
    }


    private ValidationResult passwordValidator(String pass1) {

        if(pass1 == null || pass1.length() < 8) {
            return ValidationResult.error("Password  should be at least 8 characters");
        }
        if(!enablePasswordValidation) {
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String pass2 = registrationForm.getPasswordConfirmField().getValue();

        if(pass2.equals(pass1)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Password do not match");
    }

    private void showSuccess(UserDto userDto) {
        Notification notification = Notification.show("Data saved: " + userDto.getUsername());
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    }
}
