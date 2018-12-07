package com.lou.`as`.lou.jetpack.architecture.databinding.observedata

import android.databinding.ObservableField
import android.databinding.ObservableInt

class ObservableUser {
    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val age = ObservableInt()
}