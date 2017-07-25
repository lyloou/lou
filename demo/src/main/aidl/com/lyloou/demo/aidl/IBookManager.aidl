package com.lyloou.demo.aidl;

// Declare any non-default types here with import statements

import com.lyloou.demo.aidl.Book;
import com.lyloou.demo.aidl.IOnNewBookArrivedListener;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}