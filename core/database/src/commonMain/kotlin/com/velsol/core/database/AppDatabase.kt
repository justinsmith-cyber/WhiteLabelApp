package com.velsol.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaceholderEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase()
