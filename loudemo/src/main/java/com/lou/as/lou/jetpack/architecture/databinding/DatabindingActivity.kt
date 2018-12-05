package com.lou.`as`.lou.jetpack.architecture.databinding

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lou.`as`.lou.R
import com.lou.`as`.lou.databinding.ActivityDatabindingBinding
import com.lou.`as`.lou.jetpack.architecture.databinding.data.User
import com.lou.`as`.lou.jetpack.architecture.databinding.handler.MyHandler

class DatabindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDatabindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding)

        binding.user = User("Lyloou", "Li")


        binding.handler = MyHandler()
        binding.handler2 = MyHandler()

        binding.userList = listOf(User("uF1", "uL1"), User("uF2", "uL2"))
    }
}
