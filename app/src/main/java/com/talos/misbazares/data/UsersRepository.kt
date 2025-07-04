package com.talos.misbazares.data

import com.talos.misbazares.data.db.UsersDAO
import com.talos.misbazares.data.db.model.UsersEntity

class UsersRepository(
    private val usersDAO: UsersDAO
) {

    // Create
    suspend fun insertUser(user: UsersEntity) {
        usersDAO.insertUser(user)
    }

    suspend fun insertUsers(users: List<UsersEntity>) {
        usersDAO.insertUsers(users)
    }

    // Read
    suspend fun getAllUsers(): List<UsersEntity> = usersDAO.getAllUsers()

    suspend fun getUserById(userId: Long): UsersEntity? {
        return usersDAO.getUserById(userId)
    }


    suspend fun getAllSellers(): List<UsersEntity> = usersDAO.getAllSellers()


    suspend fun login(userName: String, password: String): UsersEntity? {
        return usersDAO.login(userName, password)
    }

    // Update
    suspend fun updateUser(user: UsersEntity) {
        usersDAO.updateUser(user)
    }

    suspend fun updateUsers(users: List<UsersEntity>) {
        usersDAO.updateUsers(users)
    }

    // Delete
    suspend fun deleteUser(user: UsersEntity) {
        usersDAO.deleteUser(user)
    }
}
