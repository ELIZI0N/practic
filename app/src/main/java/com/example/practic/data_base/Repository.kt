package com.example.practic.data_base

import android.content.Context
import com.example.practic.data.Manga
import com.example.practic.data.MangaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) {
    private val dbHelper = dataBaseHelper(context)

    suspend fun getAllMangas(sortBy: String?, sortOrder: String?): List<Manga> = withContext(Dispatchers.IO) {
        dbHelper.getAllMangas(sortBy, sortOrder)
    }

    suspend fun getMangaById(id: Int): Manga? = withContext(Dispatchers.IO) {
        dbHelper.getMangaById(id)
    }

    suspend fun updateManga(manga: Manga): Int = withContext(Dispatchers.IO) {
        dbHelper.updateManga(manga)
    }

    suspend fun deleteManga(id: Int): Int = withContext(Dispatchers.IO) {
        dbHelper.deleteManga(id)
    }

    suspend fun getAllMangaTypes(): List<MangaType> = withContext(Dispatchers.IO) {
        dbHelper.getAllMangaTypes()
    }

    suspend fun getMangaTypeById(id: Int): MangaType? = withContext(Dispatchers.IO) {
        dbHelper.getMangaTypeById(id)
    }

    suspend fun insertMangaType(mangaType: MangaType): Long = withContext(Dispatchers.IO) {
        dbHelper.insertMangaType(mangaType)
    }
}