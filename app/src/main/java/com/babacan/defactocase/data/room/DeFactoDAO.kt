package com.babacan.defactocase.data.room

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.babacan.defactocase.domain.model.Movie
import com.babacan.defactocase.domain.model.User

@Dao
interface DeFactoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(favorite: Movie)

    @Query("DELETE FROM favorites WHERE imdbID = :movieId")
    suspend fun deleteFavoriteMovie(movieId: String): Int

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavoriteMovies(): List<Movie>

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE imdbID = :movieId)")
    suspend fun isMovieInFavorites(movieId: String): Boolean

    @Query("DELETE FROM favorites WHERE listName = :listName")
    suspend fun deleteAllFavoriteMoviesByListName(listName: String): Int

    @Query("UPDATE favorites SET listName = :currentListName WHERE listName = :previousListName")
    fun updateFavoriteListName(currentListName: String, previousListName: String): Int

    @Query("UPDATE favorites SET listName = :currentListName WHERE listName = :previousListName AND imdbID = :movieId")
    fun updateMoviesListName(
        currentListName: String,
        previousListName: String,
        movieId: String
    ): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResult(searchResult: SearchResult)

    @Query(" DELETE FROM search_results WHERE _id NOT IN ( SELECT _id FROM search_results ORDER BY _id DESC LIMIT 10)")
    suspend fun deleteOldSearchesKeepingLatest10(): Int

    @Query("SELECT * FROM search_results ORDER BY _id DESC")
    suspend fun getAllSearchResults(): List<SearchResult>

    // check if email is already registered
    @Query("SELECT * FROM authentication WHERE email = :email")
    suspend fun checkEmail(email: String): User?

    //insert User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    //check if user mail and password match
    @Query("SELECT * FROM authentication WHERE email = :email AND password = :password")
    suspend fun checkUser(email: String, password: String): User?

    //get logged in user
    @Query("SELECT * FROM authentication WHERE isLoggedIn = 1")
    suspend fun getLoggedInUser(): User?

    // update logged in status
    @Query("UPDATE authentication SET isLoggedIn = :loggedIn WHERE _id = :userId")
    suspend fun updateLoggedInStatus(userId: Int, loggedIn: Boolean): Int

    //check if password is matched
    @Query("SELECT * FROM authentication WHERE password = :password AND _id = :userId")
    suspend fun checkPassword(password: String, userId: Int): User?

    // check if password is matched then update user password
    @Query("UPDATE authentication SET password = :newPassword WHERE _id = :userId")
    suspend fun updateUserPassword(userId: Int, newPassword: String): Int


}

@Entity(tableName = "search_results")
data class SearchResult(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val searchWord: String,
)
