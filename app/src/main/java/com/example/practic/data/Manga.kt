package com.example.practic.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Manga(
    @SerializedName("images")
    val images: Images,
    val title: String,
    val chapters: Int? = null,
    val score: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val members: Int? = null,
    val synopsis: String? = null,
    @SerializedName("authors")
    val authors: List<Author>? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Images::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createTypedArrayList(Author.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(images, flags)
        parcel.writeString(title)
        parcel.writeValue(chapters)
        parcel.writeValue(score)
        parcel.writeValue(rank)
        parcel.writeValue(popularity)
        parcel.writeValue(members)
        parcel.writeString(synopsis)
        parcel.writeTypedList(authors)
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

data class Images(
    @SerializedName("jpg")
    val image_url: ImageItem
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ImageItem::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(image_url, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Images> {
        override fun createFromParcel(parcel: Parcel): Images {
            return Images(parcel)
        }

        override fun newArray(size: Int): Array<Images?> {
            return arrayOfNulls(size)
        }
    }
}

data class ImageItem(
    val image_url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageItem> {
        override fun createFromParcel(parcel: Parcel): ImageItem {
            return ImageItem(parcel)
        }

        override fun newArray(size: Int): Array<ImageItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class Author(
    val name: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Author> {
        override fun createFromParcel(parcel: Parcel): Author {
            return Author(parcel)
        }

        override fun newArray(size: Int): Array<Author?> {
            return arrayOfNulls(size)
        }
    }
}
