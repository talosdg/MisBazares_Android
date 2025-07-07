package com.talos.misbazares.data

import android.content.Context
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

    fun loadLoginInfo(context: Context): Pair<Long, Int>? {
        val prefs = context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("userId", -1)
        val userRol = prefs.getInt("userRol", -1)
        return if (userId != -1L && userRol != -1) Pair(userId, userRol) else null
    }
    // Update
    suspend fun updateUser(user: UsersEntity) {
        usersDAO.updateUser(user)
    }

    suspend fun updateUsers(users: List<UsersEntity>) {
        usersDAO.updateUsers(users)
    }

    fun saveLoginInfo(context: Context, userId: Long, userRol: Int) {
        val prefs = context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putLong("userId", userId)
            .putInt("userRol", userRol)
            .apply()
    }
    // Delete
    suspend fun deleteUser(user: UsersEntity) {
        usersDAO.deleteUser(user)
    }

    fun clearLoginInfo(context: Context) {
        val prefs = context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
