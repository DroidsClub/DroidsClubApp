package com.example.androidclubapp.database

import android.content.Context
import android.webkit.WebViewDatabase.getInstance
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidclubapp.models.UserData
import com.example.androidclubapp.utils.GsonUtils
import com.example.androidclubapp.utils.ioThread
import kotlinx.coroutines.flow.Flow

class DatabaseService(applicationContext: Context) {

    private val db = AppDatabase.getInstance(applicationContext)

    private val userDataDao = db.userDataDao()

    fun getUsersDataAsync(userId: String, callback: (List<UserData>) -> Unit) = ioThread { callback(getUsersData(userId)) }

    fun getLatestUsersBoxesAsync(userId: String, callback: (UserData?) -> Unit) = ioThread { callback(getLatestUsersData(userId)) }

    fun getUsersData(userId: String): List<UserData> = userDataDao.getAllUsersFavs(userId).sortedBy { it.updatedAt }.reversed()

    private fun getLatestUsersData(userId: String): UserData? = run {

        val x: List<UserData> = userDataDao.getAllUsersFavs(userId)

        return x.firstOrNull()//.maxByOrNull { it.updatedAt ?: "" }
    }

    fun insertAllUserData(vararg userData: UserData) = userDataDao.insertAll(*userData)
    fun insertUserData(userData: UserData)= userDataDao.insertUserData(userData)

    fun updateUserData(userData: UserData) = userDataDao.updateUserData(userData)

    fun deleteAllUserData() = userDataDao.deleteAllUserData()
    fun deleteUserData(userData: UserData) = userDataDao.delete(userData)
    fun deleteUsersData(userId: String) = userDataDao.deleteUsersData(userId)
}