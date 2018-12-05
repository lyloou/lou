package com.lou.`as`.lou.jetpack.architecture.databinding.handler

import android.view.View
import android.widget.TextView
import android.widget.Toast

class MyHandler {
    fun onClickFriend(view: View) {
        when (view) {
            is TextView -> {
                Toast.makeText(view.context, view.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}