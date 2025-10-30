package com.example.practic.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

    @Query("SELECT * FROM Manga")
    fun getAllMangas(): Flow<List<Manga>>

    @Query("SELECT * FROM Manga ORDER BY ID ASC")
    fun getAllMangasByIdAsc(): Flow<List<Manga>>

    @Query("SELECT * FROM Manga ORDER BY `Release` DESC")
    fun getAllMangasByReleaseDesc(): Flow<List<Manga>>

    @Query("SELECT * FROM Manga ORDER BY Chapters DESC")
    fun getAllMangasByChaptersDesc(): Flow<List<Manga>>

    @Query("SELECT * FROM Manga ORDER BY Score DESC")
    fun getAllMangasByScoreDesc(): Flow<List<Manga>>

    @Query("SELECT * FROM Manga ORDER BY Popularity ASC")
    fun getAllMangasByPopularityAsc(): Flow<List<Manga>>

    @Query("SELECT * FROM Manga WHERE ID = :id")
    fun getMangaById(id: Int): Flow<Manga?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManga(manga: Manga): Long // Явно указываем Long

    @Update
    suspend fun updateManga(manga: Manga): Int // Явно указываем Int

    @Delete
    suspend fun deleteManga(manga: Manga): Int // Явно указываем Int

    @Query("DELETE FROM Manga WHERE ID = :id")
    suspend fun deleteMangaById(id: Int): Int // Явно указываем Int

    @Query("SELECT * FROM Manga WHERE Name LIKE '%' || :query || '%' OR Authors LIKE '%' || :query || '%' OR Type LIKE '%' || :query || '%'")
    fun searchManga(query: String): Flow<List<Manga>>

    @Query("SELECT * FROM Manga ORDER BY Popularity ASC LIMIT :limit")
    fun getTopManga(limit: Int): Flow<List<Manga>>
}
