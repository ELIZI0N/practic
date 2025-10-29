package com.example.practic.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.practic.data.Manga
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class dataBaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Manga_DB.db"
        private const val DATABASE_VERSION = 1
        private const val TAG = "DataBaseHelper"

        // Manga table
        const val TABLE_MANGA = "Manga"
        const val COLUMN_ID = "ID"
        const val COLUMN_NAME = "Name"
        const val COLUMN_IMAGES = "Images"
        const val COLUMN_AUTHORS = "Authors"
        const val COLUMN_RELEASE = "Release"
        const val COLUMN_VIEWS = "Views"
        const val COLUMN_CHAPTERS = "Chapters"
        const val COLUMN_SCORE = "Score"
        const val COLUMN_SYNOPSIS = "Synopsis"
        const val COLUMN_POPULARITY = "Popularity"
        const val COLUMN_TYPE = "Type"
    }

    init {
        copyDataBase()
    }

    private fun copyDataBase() {
        val dbFile = context.getDatabasePath(DATABASE_NAME)

        if (!dbFile.exists()) {
            try {
                dbFile.parentFile?.mkdirs()
                val inputStream: InputStream = context.assets.open(DATABASE_NAME)
                val outputStream = FileOutputStream(dbFile)

                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
                Log.i(TAG, "Database copied successfully")
            } catch (e: IOException) {
                Log.e(TAG, "Error copying database", e)
                throw Error("Error copying database")
            }
        } else {
            Log.i(TAG, "Database already exists")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun getAllMangas(): List<Manga> {
        return getAllMangasSorted(null, null)
    }

    fun getAllMangasSorted(sortBy: String?, sortOrder: String?): List<Manga> {
        val mangaList = mutableListOf<Manga>()
        val db = readableDatabase

        val orderByClause = when {
            sortBy != null && sortOrder != null -> {
                val validSortBy = when (sortBy) {
                    "ID" -> COLUMN_ID
                    "Release" -> COLUMN_RELEASE
                    "Views" -> COLUMN_VIEWS
                    "Chapters" -> COLUMN_CHAPTERS
                    "Score" -> COLUMN_SCORE
                    "Popularity" -> COLUMN_POPULARITY
                    else -> COLUMN_ID // По умолчанию сортируем по ID
                }
                val validSortOrder = if (sortOrder.equals("DESC", ignoreCase = true)) "DESC" else "ASC"
                "$validSortBy $validSortOrder"
            }
            else -> null
        }

        val cursor = db.query(TABLE_MANGA, null, null, null, null, null, orderByClause)

        cursor.use {
            while (it.moveToNext()) {
                val manga = Manga(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    images = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGES)),
                    authors = it.getString(it.getColumnIndexOrThrow(COLUMN_AUTHORS)),
                    release = it.getInt(it.getColumnIndexOrThrow(COLUMN_RELEASE)),
                    views = it.getString(it.getColumnIndexOrThrow(COLUMN_VIEWS)),
                    chapters = it.getInt(it.getColumnIndexOrThrow(COLUMN_CHAPTERS)),
                    score = it.getDouble(it.getColumnIndexOrThrow(COLUMN_SCORE)),
                    synopsis = it.getString(it.getColumnIndexOrThrow(COLUMN_SYNOPSIS)),
                    popularity = it.getInt(it.getColumnIndexOrThrow(COLUMN_POPULARITY)),
                    type = it.getString(it.getColumnIndexOrThrow(COLUMN_TYPE))
                )
                mangaList.add(manga)
            }
        }

        return mangaList
    }

    fun getMangaById(id: Int): Manga? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MANGA,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return Manga(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    images = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGES)),
                    authors = it.getString(it.getColumnIndexOrThrow(COLUMN_AUTHORS)),
                    release = it.getInt(it.getColumnIndexOrThrow(COLUMN_RELEASE)),
                    views = it.getString(it.getColumnIndexOrThrow(COLUMN_VIEWS)),
                    chapters = it.getInt(it.getColumnIndexOrThrow(COLUMN_CHAPTERS)),
                    score = it.getDouble(it.getColumnIndexOrThrow(COLUMN_SCORE)),
                    synopsis = it.getString(it.getColumnIndexOrThrow(COLUMN_SYNOPSIS)),
                    popularity = it.getInt(it.getColumnIndexOrThrow(COLUMN_POPULARITY)),
                    type = it.getString(it.getColumnIndexOrThrow(COLUMN_TYPE))
                )
            }
        }

        return null
    }

    fun updateManga(manga: Manga): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, manga.name)
            put(COLUMN_IMAGES, manga.images)
            put(COLUMN_AUTHORS, manga.authors)
            put(COLUMN_RELEASE, manga.release)
            put(COLUMN_VIEWS, manga.views)
            put(COLUMN_CHAPTERS, manga.chapters)
            put(COLUMN_SCORE, manga.score)
            put(COLUMN_SYNOPSIS, manga.synopsis)
            put(COLUMN_POPULARITY, manga.popularity)
            put(COLUMN_TYPE, manga.type)
        }
        return db.update(TABLE_MANGA, values, "$COLUMN_ID = ?", arrayOf(manga.id.toString()))
    }

    fun deleteManga(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_MANGA, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun addManga(manga: Manga): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, manga.name)
            put(COLUMN_IMAGES, manga.images)
            put(COLUMN_AUTHORS, manga.authors)
            put(COLUMN_RELEASE, manga.release)
            put(COLUMN_VIEWS, manga.views)
            put(COLUMN_CHAPTERS, manga.chapters)
            put(COLUMN_SCORE, manga.score)
            put(COLUMN_SYNOPSIS, manga.synopsis)
            put(COLUMN_POPULARITY, manga.popularity)
            put(COLUMN_TYPE, manga.type)
        }
        return db.insert(TABLE_MANGA, null, values)
    }
}