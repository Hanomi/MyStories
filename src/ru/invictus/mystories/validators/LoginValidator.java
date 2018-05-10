package ru.invictus.mystories.validators;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

@FacesValidator("loginValidator")
public class LoginValidator implements Validator {
    private final static Set<String> names = new HashSet<>();

    static {
        names.add("login");
        names.add("admin");
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if (o.toString().length() < 5) {
            validateError("login_error_length", facesContext);
        }
        if (!Character.isLetter(o.toString().charAt(0))) {
            validateError("login_error_number", facesContext);
        }
        if (names.contains(o.toString())) {
            validateError("login_error_used", facesContext);
        }
    }

    private void validateError(String string, FacesContext facesContext) {
        ResourceBundle bundle = ResourceBundle.getBundle("locale.localisation", facesContext.getViewRoot().getLocale());
        FacesMessage message = new FacesMessage(bundle.getString(string));
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(message);
    }
}
