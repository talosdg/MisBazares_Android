package com.talos.misbazares.application

import android.app.Application
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.db.BazaresDatabase

class EventsDBApp: Application() {
    private val database by lazy {
        BazaresDatabase.getDatabase(this@EventsDBApp)
    }

    val repository by lazy{
        EventsRepository(database.eventsDao())
    }
/*
    val usersRepository by lazy {
        UsersRepository(database.usersDao())
    }*/
}