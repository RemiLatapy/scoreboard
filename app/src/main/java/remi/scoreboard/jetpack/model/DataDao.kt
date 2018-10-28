package remi.scoreboard.jetpack.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Insert
    fun insert(game: Game)

    @Query("SELECT * FROM games")
    fun loadAllGames(): LiveData<List<Game>>

    @Query("DELETE FROM games")
    fun deleteAll()

    @Query("SELECT * FROM games WHERE game_id = :id")
    fun loadGameById(id: Int): LiveData<Game>
}

@Dao
interface MatchDao {
    @Insert
    fun insert(match: Match)

    @Query("SELECT * FROM matches")
    fun loadAllMatches(): LiveData<List<Match>>

    @Query("DELETE FROM matches")
    fun deleteAll()

    @Query("SELECT * FROM matches WHERE match_id = :id")
    fun loadMatchById(id: Int): LiveData<Match>
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