package com.lyloou.test.util;

public interface PermissionListener {
    String name();

    Runnable whenShouldShowRequest();

    Runnable whenGranted();
}
