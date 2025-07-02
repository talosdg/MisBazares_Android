package com.talos.misbazares.application

import android.app.Application
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.UsersRepository
import com.talos.misbazares.data.db.BazaresDatabase
import com.talos.misbazares.ui.detailevent.DetailViewModelFactory

class EventsDBApp: Application() {
    private val database by lazy {
        BazaresDatabase.getDatabase(this@EventsDBApp)
    }

    val eventsRepository by lazy { // Cambié 'repository' a 'eventsRepository' para claridad
        EventsRepository(database.eventsDao())
    }

    val usersRepository by lazy {
        UsersRepository(database.usersDao())
    }

    // propiedad para la fábrica del ViewModel compartido
    val DetailViewModelFactory by lazy {
        DetailViewModelFactory(eventsRepository)
    }
}