package remi.scoreboard.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.parse.ParseACL
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import remi.scoreboard.data.*


class UserRepository {

    lateinit var currentUser: LiveData<User>
    val allUsers: LiveData<List<User>> = UserDao.loadAll()

    val storedCurrentUser: LiveData<User>? by lazy {
        val user = ParseUser.getCurrentUser()
        if (user != null)
//            refreshCurrentUser(user) // TODO call from thread + check last update to avoid fetch
            UserDao.load(user.objectId) // find current user in DB (might be null)
        else
            null
    }

    @WorkerThread
    suspend fun refreshCurrentUser(user: ParseUser) {
        user.fetch()
        UserDao.update(User(ParseUser()))
    }


    @WorkerThread
    suspend fun insert(user: User) = UserDao.insert(user)

    @WorkerThread
    suspend fun deleteAll() = UserDao.deleteAll()

    @WorkerThread
    suspend fun createUser(user: User): String {
        Log.d("THREAD", "createUser/Scope/repository = #${Thread.currentThread().id} // ${Thread.currentThread()}")
        val parseUser = user.getParseUser() // create parse object user
        withContext(Dispatchers.IO) {
            Log.d(
                "THREAD",
                "createUser/Scope/repository/withContext = #${Thread.currentThread().id} // ${Thread.currentThread()}"
            )
            try {
                parseUser.signUp() // sign up user in parse TODO exception
                val newUser = ParseUser.getCurrentUser() // get back parse managed user
                newUser.acl = ParseACL(parseUser) // set ACL
                newUser.saveInBackground() // save user with new ACL value TODO callback
                UserDao.insert(User(newUser)) // create user in DB

                Log.d(
                    "THREAD",
                    "createUser/Scope/repository/withContext2 = #${Thread.currentThread().id} // ${Thread.currentThread()}"
                )
            } catch (e: ParseException) {
                Log.e("LOGIN", "Signup failed: ${e.message}")
            }
        }
        return parseUser.objectId
    }

    fun user(id: String): LiveData<User>? = UserDao.load(id)
}

class MatchRepository {

    val allMatches: LiveData<List<Match>> = MatchDao.loadAll()

    @WorkerThread
    suspend fun insert(match: Match) = MatchDao.insert(match)

    fun create(match: Match): LiveData<Match> = MatchDao.create(match)

    fun addPlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        MatchDao.addPlayer(currentMatch, playerScore)

    fun removePlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        MatchDao.removePlayer(currentMatch, playerScore)

    fun addPoints(playerScore: PlayerScore, points: Int) {
        MatchDao.addPoints(playerScore, points)
    }
}

class GameRepository {

    val allGames: LiveData<List<Game>> = GameDao.loadAll()

    @WorkerThread
    suspend fun insert(game: Game) = GameDao.insert(game)

    @WorkerThread
    suspend fun insert(games: List<Game>) = GameDao.insert(games)

    @WorkerThread
    suspend fun deleteAll() = GameDao.deleteAll()
}