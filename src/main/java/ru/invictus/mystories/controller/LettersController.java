package ru.invictus.mystories.controller;

import ru.invictus.mystories.db.DataHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@Named
@ApplicationScoped
public class LettersController implements Serializable {
    private Set<Character> letters;

    public LettersController() {
        letters = new TreeSet<>();
        //todo change query
        DataHelper.INSTANCE.getAllLetters().forEach( f -> letters.add(f.charAt(0)));
    }

    public Set<Character> getLetters() {
        return letters;
    }
}
