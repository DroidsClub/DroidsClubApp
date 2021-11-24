package com.example.androidclubapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidclubapp.models.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {
    @Query("SELECT * FROM UserData")
    fun getAll(): List<UserData>

    @Query("SELECT * FROM UserData WHERE userId == :userId")
    fun getAllUsersFavs(userId: String): List<UserData>

    @Insert
    fun insertAll(vararg userData: UserData)//: Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserData(userData: UserData)//: Flow<Long>

    @Update
    fun updateUserData(userData: UserData)

    @Delete
    fun delete(userData: UserData)//: Flow<Int>

    @Query("DELETE FROM UserData")
    fun deleteAllUserData()//: Flow<Int>

    @Query("DELETE FROM UserData WHERE userId == :userId")
    fun deleteUsersData(userId: String)//: Flow<Int>
}
