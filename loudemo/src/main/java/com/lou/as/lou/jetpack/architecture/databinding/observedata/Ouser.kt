package com.lou.`as`.lou.jetpack.architecture.databinding.observedata

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.lou.`as`.lou.BR

class Ouser : BaseObservable() {
    @get:Bindable
    var firstName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
        }

    @get:Bindable
    var lastName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
        }
}