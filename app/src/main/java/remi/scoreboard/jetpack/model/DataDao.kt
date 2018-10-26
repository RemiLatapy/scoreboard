package remi.scoreboard.jetpack.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.SkipQueryVerification

@Dao
interface GameDao {
    @Insert
    fun insert(game: Game)

    @Query("SELECT * FROM games")
    fun loadAllGames(): LiveData<List<Game>>

    @Query("DELETE FROM games")
    fun deleteAll()
}

@Dao
interface MatchDao {
    @Insert
    fun insert(match: Match)

    @Query("SELECT * FROM matches")
    fun loadAllMatches(): LiveData<List<Match>>

    @Query("DELETE FROM matches")
    fun deleteAll()
}

@Dao
interface ScoreDao {
    @Insert
    fun insert(score: Score)

    @Query("SELECT * FROM scores WHERE score_id = :id")
    fun loadScoreById(id: Int): LiveData<Score>

    @Query("DELETE FROM scores")
    fun deleteAll()
}

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM users")
    fun loadAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE user_id = :id")
    fun loadUserById(id: Int): LiveData<User>

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("UPDATE sqlite_sequence SET seq = (SELECT MAX(user_id) FROM users) WHERE name=\"users\"")
    fun resetId()
}