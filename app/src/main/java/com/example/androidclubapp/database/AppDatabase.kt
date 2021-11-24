package com.example.androidclubapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidclubapp.models.FavConverter
import com.example.androidclubapp.models.UserData

@Database(entities = [UserData::class], version = 1)
@TypeConverters(FavConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        @Synchronized fun getInstance(context: Context): AppDatabase = run {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "favourites")
                //.fallbackToDestructiveMigration().fallbackToDestructiveMigrationFrom(1).fallbackToDestructiveMigrationOnDowngrade()
                .build()
    }
}
