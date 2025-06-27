package com.talos.misbazares

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.talos.misbazares.ui.login.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        supportFragmentManager.commit {

            replace<LoginFragment>(R.id.login_container)

           //
            //setReorderingAllowed(true)
            //addToBackStack("replacement")
        }

    }





}