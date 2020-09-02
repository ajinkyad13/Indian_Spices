package com.android.indianspices.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.indianspices.model.Orders
import com.android.indianspices.model.User
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import androidx.annotation.VisibleForTesting
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.icu.lang.UCharacter.GraphemeClusterBreak.V






@Database(entities = arrayOf(Orders::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val sLock = Any()
        @VisibleForTesting
        val MIGRATION_1_2: Migration = object : Migration(1, 2)
        {
            override fun migrate(database: SupportSQLiteDatabase)
            {
                database.execSQL("ALTER TABLE 'order_detail' " + " ADD COLUMN price TEXT")
            }
        }
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "order_details.db").addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE
        }
    }





}