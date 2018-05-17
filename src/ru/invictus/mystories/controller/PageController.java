package ru.invictus.mystories.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("pageController")
@SessionScoped
public class PageController implements Serializable {
    private int booksOnPage; // кол-во книг на странице
    private int selectedPage; // текущая страница
    private int foundBooks; // кол-во книг
    private List<Integer> pager;

    public PageController() {
        booksOnPage = 5;
        selectedPage = 1;
        foundBooks = 8;
        pager = new ArrayList<>();
    }

    public void updatePager() {
        int size = (int) Math.ceil((double) foundBooks / booksOnPage);
        pager.clear();
        pager.add(1);
        for (int i = 1; i < size; i++) {
            pager.add(i + 1);
        }
    }

    public void changePage(int page_number) {
        this.selectedPage = page_number;
    }

    public boolean showPager() {
        return pager.size() > 1;
    }

    public int getFirstResult() {
        return booksOnPage * selectedPage - booksOnPage;
    }

    public int getMaxResults() {
        return booksOnPage;
    }

    public List<Integer> getPager() {
        return pager;
    }

    public int getBooksOnPage() {
        return booksOnPage;
    }

    public void setBooksOnPage(int booksOnPage) {
        this.booksOnPage = booksOnPage;
    }

    public int getSelectedPage() {
        return selectedPage;
    }

    public int getFoundBooks() {
        return foundBooks;
    }

    public void setFoundBooks(int foundBooks) {
        this.foundBooks = foundBooks;
    }


}
