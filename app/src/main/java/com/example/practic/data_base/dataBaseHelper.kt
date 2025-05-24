
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
        private const val DATABASE_NAME = "Manga_DB.db" //Имя файла БД
        private const val DATABASE_VERSION = 1
        private const val TAG = "DataBaseHelper"  // Тег для логов

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
        // Call method to copy database on initialization
        copyDataBase()
    }

    private fun copyDataBase() {
        val dbFile = context.getDatabasePath(DATABASE_NAME) // Полный путь к БД приложения

        if (!dbFile.exists()) { //Если не существует, то копируем
            try {
                dbFile.parentFile?.mkdirs() // Создаем директорию databases, если она не существует
                val inputStream: InputStream = context.assets.open(DATABASE_NAME) // Открываем поток к файлу в assets
                val outputStream = FileOutputStream(dbFile) // Открываем поток для записи в файл БД приложения

                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) { //Побайтово читаем и пишем
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
                Log.i(TAG, "Database copied successfully")
            } catch (e: IOException) {
                Log.e(TAG, "Error copying database", e)
                throw Error("Error copying database") // Выбрасываем исключение, чтобы приложение не продолжало работу с пустой БД
            }
        } else {
            Log.i(TAG, "Database already exists")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun getAllMangas(sortBy: String?, sortOrder: String?): List<Manga> {
        val mangaList = mutableListOf<Manga>()
        val db = readableDatabase

        // Construct the ORDER BY clause dynamically
        val orderByClause = if (sortBy != null && sortBy.isNotEmpty()) {
            val sortDirection = if (sortOrder != null && sortOrder.equals("DESC", ignoreCase = true)) "DESC" else "ASC"
            "ORDER BY $sortBy $sortDirection"
        } else {
            null // No sorting specified
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
}
