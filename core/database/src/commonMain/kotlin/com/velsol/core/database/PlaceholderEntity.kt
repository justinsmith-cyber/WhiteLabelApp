package com.velsol.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceholderEntity(
    @PrimaryKey val id: Long = 0,
)
