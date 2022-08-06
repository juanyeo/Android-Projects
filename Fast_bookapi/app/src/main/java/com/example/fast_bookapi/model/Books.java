package com.example.fast_bookapi.model;

import java.util.List;

public class Books {
    private String title;
    private List<Book> item;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Book> getItem() {
        return item;
    }

    public void setItem(List<Book> item) {
        this.item = item;
    }

    public String booksString() {
        String resultString = "";

        for (Book book: item) {
            resultString += book.bookString();
        }

        return resultString;
    }
}
