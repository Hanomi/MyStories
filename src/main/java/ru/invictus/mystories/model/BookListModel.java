package ru.invictus.mystories.model;


import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.invictus.mystories.entity.Book;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class BookListModel extends LazyDataModel<Book> {
    private List<Book> data;

    @Override
    public Book getRowData(String rowKey) {
        for(Book book : data) {
            if(book.getId() == Long.parseLong(rowKey))
                return book;
        }
        return null;
    }

    @Override
    public Object getRowKey(Book book) {
        return book.getId();
    }

    @Override
    public List<Book> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
   /*     pager.setFrom(first);
        pager.setTo(pageSize);

        dataHelper.populateList();

        this.setRowCount(pager.getTotalBooksCount());

        return pager.getList();*/
        return Collections.emptyList();
    }
}
