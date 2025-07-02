package com.talos.misbazares.data

import androidx.room.Dao
import com.talos.misbazares.data.db.UsersDAO
import com.talos.misbazares.data.db.model.UsersEntity

@Dao
class UsersRepository(
    private val usersDAO: UsersDAO
) {
    suspend fun insertEvent(user: UsersEntity){
        usersDAO.insertUser(user)
    }
    suspend fun getAllUsers(): MutableList<UsersEntity> =
        usersDAO.getAllUsers()

    suspend fun updateUser(user: UsersEntity){
        usersDAO.updateUser(user)
    }
    suspend fun deleteUser(user: UsersEntity){
        usersDAO.deleteUser(user)
    }
}