package com.lyloou.demo.aidl;

// Declare any non-default types here with import statements

import com.lyloou.demo.aidl.Book;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}