package com.talos.misbazares

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.talos.misbazares.ui.sellerevents.SellersEventsFragment

class SellersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sellers)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.sellers_container, SellersEventsFragment())
                .commit()
        }
    }
}
