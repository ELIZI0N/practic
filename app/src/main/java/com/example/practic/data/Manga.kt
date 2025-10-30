package com.example.practic.data

import android.os.Parcelable
import android.os.Parcel
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Manga")
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

    @ColumnInfo(name = "Type")
    val type: String,

    /*@ColumnInfo(name = "comments", defaultValue = "0")
    val comments: Long = 0L,*/

    /*@ColumnInfo(name = "status", defaultValue = "'active'")
    val status: String = "active"*/

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString() ?: "",
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString() ?: "",
        //parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(images)
        parcel.writeString(authors)
        parcel.writeInt(release)
        parcel.writeString(views)
        parcel.writeValue(chapters)
        parcel.writeValue(score)
        parcel.writeString(synopsis)
        parcel.writeValue(popularity)
        parcel.writeString(type)
        //parcel.writeLong(comments)
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