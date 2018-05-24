package ru.invictus.mystories.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;

@Named
@SessionScoped
public class LocaleChanger implements Serializable {

    // default ru
    private Locale currentLocale = new Locale("ru");

    public LocaleChanger() {}

    public void changeLocale(String localeCode) {
        currentLocale = new Locale(localeCode);
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }
}
