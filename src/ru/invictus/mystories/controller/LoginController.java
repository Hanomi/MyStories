package ru.invictus.mystories.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class LoginController {
    public LoginController() {
    }

    public String login() {
        return "books";
    }
}
