package com.babacan.defactocase.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.domain.model.User

@Database(
    entities = [Movie::class, SearchResult::class, User::class],
    version = 6,
    exportSchema = false
)
abstract class DeFactoDatabase : RoomDatabase() {
    abstract fun deFactoDao(): DeFactoDAO
}