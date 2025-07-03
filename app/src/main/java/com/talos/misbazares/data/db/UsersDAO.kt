package com.talos.misbazares.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.talos.misbazares.data.db.model.UsersEntity
import com.talos.misbazares.util.Constants

@Dao
interface UsersDAO {
    // Create
    @Insert
    suspend fun insertUser(user: UsersEntity)

    @Insert
    suspend fun insertUsers(users: List<UsersEntity>)

    // Read
    @Query("SELECT * FROM ${Constants.DATABASE_USERS_TABLE}")
    suspend fun getAllUsers(): List<UsersEntity>

    @Query("SELECT * FROM ${Constants.DATABASE_USERS_TABLE} WHERE user_id = :userId")
    suspend fun getUserById(userId: Long): UsersEntity?

    @Query("SELECT * FROM users_table WHERE user_name = :userName AND user_password = :password LIMIT 1")
    suspend fun login(userName: String, password: String): UsersEntity?


    // Update
    @Update
    suspend fun updateUser(user: UsersEntity)

    @Update
    suspend fun updateUsers(users: List<UsersEntity>)

    // Delete
    @Delete
    suspend fun deleteUser(user: UsersEntity)
}
