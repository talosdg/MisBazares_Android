package com.talos.misbazares.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.data.db.model.UsersEntity
import com.talos.misbazares.util.Constants

@Database(
    entities = [EventEntity::class, UsersEntity::class], // en los corchetes , UserEntity::class
    version = 1,
    exportSchema = true // se obvió para migración
)

abstract class BazaresDatabase : RoomDatabase() {
    // Aquí los DAO con anotaciones
    abstract fun eventsDao(): EventsDAO
    abstract fun usersDao(): UsersDAO

    companion object{
        @Volatile
        private var INSTANCE: BazaresDatabase? = null
        fun getDatabase(context: Context): BazaresDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BazaresDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}