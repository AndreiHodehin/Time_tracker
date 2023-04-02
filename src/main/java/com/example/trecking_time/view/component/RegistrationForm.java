package com.example.trecking_time.view.component;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Scope("prototype")
public class RegistrationForm extends FormLayout {

    private final H3 title;
    private final TextField username;
    private final PasswordField password;
    private final PasswordField passwordConfirm;
    private final Span errorMessageField;
    private final Button submitButton;


    public RegistrationForm() {
        title = new H3("Signup form");
        username = new TextField("Username");
        password = new PasswordField("Password");
        passwordConfirm = new PasswordField("Confirm password");

        setRequiredIndicatorVisible(username, password, passwordConfirm);

        errorMessageField = new Span();
        submitButton = new Button("Submit");
        add(title,username, password, passwordConfirm, errorMessageField, submitButton);

        setMaxWidth("500px");

        setResponsiveSteps(
                new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("490px", 1, ResponsiveStep.LabelsPosition.TOP));

        setColspan(title, 2);
        setColspan(errorMessageField, 2);
        setColspan(submitButton, 2);
    }

    public PasswordField getPasswordField() { return password; }

    public PasswordField getPasswordConfirmField() { return passwordConfirm; }

    public Span getErrorMessageField() { return errorMessageField; }

    public Button getSubmitButton() { return submitButton; }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }

}
