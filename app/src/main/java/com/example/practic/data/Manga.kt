package com.example.practic.data

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString() ?: "",
        images = parcel.readString() ?: "",
        authors = parcel.readString() ?: "",
        release = parcel.readInt(),
        views = parcel.readString(),
        chapters = parcel.readInt(),
        score = parcel.readDouble(),
        synopsis = parcel.readString() ?: "",
        popularity = parcel.readInt(),
        typeId = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(images)
        parcel.writeString(authors)
        parcel.writeInt(release)
        parcel.writeString(views)
        chapters?.let { parcel.writeInt(it) }
        score?.let { parcel.writeDouble(it) }
        parcel.writeString(synopsis)
        parcel.writeInt(popularity ?: 0)
        parcel.writeInt(typeId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Manga> {
        override fun createFromParcel(parcel: Parcel): Manga {
            return Manga(parcel)
        }

        override fun newArray(size: Int): Array<Manga?> {
            return arrayOfNulls(size)
        }
    }
}