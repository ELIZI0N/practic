package com.example.practic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Manga",
    foreignKeys = [ForeignKey(
        entity = MangaType::class,
        parentColumns = ["ID"],
        childColumns = ["Type_ID"],
        onDelete = ForeignKey.CASCADE
    )])
data class Manga(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int = 0,

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Images")
    val images: String,

    @ColumnInfo(name = "Authors")
    val authors: String,

    @ColumnInfo(name = "Release")
    val release: Int,

    @ColumnInfo(name = "Views")
    val views: String?,

    @ColumnInfo(name = "Chapters")
    val chapters: Int?,

    @ColumnInfo(name = "Score")
    val score: Double?,

    @ColumnInfo(name = "Synopsis")
    val synopsis: String,

    @ColumnInfo(name = "Popularity")
    val popularity: Int?,

    @ColumnInfo(name = "Type_ID")
    val typeId: Int
)
