package ru.invictus.mystories.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class User implements Serializable {
    private String username;
    private String password;

    private Logger logger = Logger.getLogger(User.class.getName());

    public User() {
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String  login() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            ((HttpServletRequest) context.getExternalContext().getRequest()).login(username, password);
            return "books";
        } catch (ServletException e) {
            logger.log(Level.SEVERE, null, e);
            ResourceBundle bundle = ResourceBundle.getBundle("locale.localisation", context.getViewRoot().getLocale());
            FacesMessage message = new FacesMessage(bundle.getString("login_or_password_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("login_form", message);
        }
        return "index";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest servletRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            servletRequest.logout();
        } catch (ServletException e) {
            logger.log(Level.SEVERE, null, e);
        }
        context.getExternalContext().invalidateSession();
        return "/index.xhml?faces-redirect=true";
    }
}
