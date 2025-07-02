package com.talos.misbazares.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
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
    suspend fun insertUser(users: MutableList<UsersEntity>)

    // Read
    @Query("SELECT * FROM ${Constants.DATABASE_USERS_TABLE}")
    suspend fun getAllUsers(): MutableList<UsersEntity>

    @Query("SELECT * FROM ${Constants.DATABASE_USERS_TABLE } WHERE user_id = :userId")
    suspend fun getAllUsers(userId: Long): UsersEntity?

    @Transaction

    // Update
    @Update
    suspend fun updateUser(user: UsersEntity)

    @Update
    suspend fun updateUser(user: MutableList<UsersEntity>)

    // Delete
    @Delete
    suspend fun deleteUser(user: UsersEntity)


}