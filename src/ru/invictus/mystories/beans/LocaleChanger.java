package ru.invictus.mystories.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;

@Named
@SessionScoped
public class LocaleChanger implements Serializable {

    private Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();

    public LocaleChanger() {}

    public void changeLocale(String localeCode) {
        currentLocale = new Locale(localeCode);
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }
}
