package com.talos.misbazares.application

import android.app.Application
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.UsersRepository
import com.talos.misbazares.data.db.BazaresDatabase
import com.talos.misbazares.data.db.model.UsersEntity
import com.talos.misbazares.ui.detailevent.DetailViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    val inscriptionRepository by lazy {
        InscriptionRepository(database.inscriptionDao())
    }

    val DetailViewModelFactory by lazy {
        DetailViewModelFactory(eventsRepository)
    }
    override fun onCreate() {
        super.onCreate()

        val database = BazaresDatabase.getDatabase(this)

        prepopulateUsers()
    }

    private fun prepopulateUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val existing = usersRepository.getAllUsers()
            if (existing.isEmpty()) {
                usersRepository.insertUser(
                    UsersEntity(
                        name = "martin",
                        secondname = "",
                        email = "martin@bazares.com",
                        password = "admin123",
                        rol = 2,
                        events = ""
                    )
                )
                usersRepository.insertUser(
                    UsersEntity(
                        name = "juana",
                        secondname = "",
                        email = "juana@bazares.com",
                        password = "clave123",
                        rol = 0,
                        events = ""
                    )
                )
                usersRepository.insertUser(
                    UsersEntity(
                        name = "ana",
                        secondname = "Gómez",
                        email = "ana@bazares.com",
                        password = "clave123",
                        rol = 0,
                        events = ""
                    )
                )
                usersRepository.insertUser(
                    UsersEntity(
                        name = "gina",
                        secondname = "López",
                        email = "gina@bazares.com",
                        password = "admin123",
                        rol = 2,
                        events = ""
                    )
                )
                usersRepository.insertUser(
                    UsersEntity(
                        name = "angel",
                        secondname = "Pérez",
                        email = "angel@bazares.com",
                        password = "clave123",
                        rol = 0,
                        events = ""
                    )
                )
                usersRepository.insertUser(
                    UsersEntity(
                        name = "marco",
                        secondname = "Vazquez",
                        email = "marco@bazares.com",
                        password = "clave123",
                        rol = 0,
                        events = ""
                    )
                )
            }
        }
    }
}