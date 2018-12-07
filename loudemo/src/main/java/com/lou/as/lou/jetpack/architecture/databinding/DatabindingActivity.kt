package com.lou.`as`.lou.jetpack.architecture.databinding

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.lou.`as`.lou.R
import com.lou.`as`.lou.databinding.ActivityDatabindingBinding
import com.lou.`as`.lou.jetpack.architecture.databinding.data.User
import com.lou.`as`.lou.jetpack.architecture.databinding.handler.MyHandler
import com.lou.`as`.lou.jetpack.architecture.databinding.observedata.ObservableUser
import com.lou.`as`.lou.jetpack.architecture.databinding.observedata.Ouser

class DatabindingActivity : AppCompatActivity() {

    private val observableUser = ObservableUser()
    private val ouser = Ouser().apply {
        firstName = "oF1"
        lastName = "oL1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDatabindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding)

        val user = User("Lyloou", "Li")
        binding.user = user
        binding.ouser = ouser


        binding.handler = MyHandler()
        binding.handler2 = MyHandler()

        binding.userList = listOf(User("uF1", "uL1"), User("uF2", "uL2"))
        observableUser.firstName.set("observeF1")
        observableUser.lastName.set("observeL1")
        observableUser.age.set(18)
        binding.observableUser = observableUser
    }

    private var counter = 0
    fun changeObservableUser(view: View) {
        observableUser.firstName.set("observeF${++counter}")
        observableUser.lastName.set("observeL$counter")
        observableUser.age.set(19 + counter)

        ouser.firstName = ouser.firstName + counter
        println(ouser)
    }
}
