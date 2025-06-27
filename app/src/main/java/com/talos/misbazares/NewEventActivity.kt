package com.talos.misbazares

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.replace
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.talos.misbazares.ui.trigger.NewEventFragment

class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_event)

        supportFragmentManager.commit {
            replace<NewEventFragment>(R.id.fragment_events)
        }

    }


}