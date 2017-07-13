package com.lyloou.demo.aidl;

// Declare any non-default types here with import statements

import com.lyloou.demo.aidl.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}