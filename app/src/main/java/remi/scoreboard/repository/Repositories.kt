package remi.scoreboard.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.parse.ParseACL
import com.parse.ParseException
import com.parse.ParseUser
import io.realm.exceptions.RealmException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import remi.scoreboard.data.*


class UserRepository {

    val signupState = MutableLiveData<MessageStatus>()

    val currentUser: MediatorLiveData<Resource<User>> by lazy {
        val ret = MediatorLiveData<Resource<User>>()
        val user = ParseUser.getCurrentUser()
        if (user != null) {
            // TODO try to fetch and update db here
            val liveUser = UserDao.load(user.objectId) // find current user in DB (might be null)
            if (liveUser.value != null)
                ret.addSource(liveUser) { value -> ret.value = Resource.success(value) }
        }
        ret
    }

    val allUsers: LiveData<List<User>> = UserDao.loadAll()

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
    suspend fun createUser(user: User) {
        signupState.postValue(MessageStatus(Status.LOADING)) // Inform UI
        Log.d("THREAD", "createUser/Scope/repository = #${Thread.currentThread().id} // ${Thread.currentThread()}")

        val parseUser = user.getParseUser() // create parse object user
        val insertedOk = withContext<Boolean>(Dispatchers.IO) {
            Log.d(
                "THREAD",
                "createUser/Scope/repository/withContext(io) = #${Thread.currentThread().id} // ${Thread.currentThread()}"
            )
            try {
                parseUser.signUp()
            } catch (e: ParseException) {
                signupState.postValue(MessageStatus(Status.ERROR, e.message.toString())) // Inform UI
                Log.e("LOGIN", "Signup failed: ${e.message}")
                return@withContext false
            }

            val newUser = ParseUser.getCurrentUser() // get back parse managed user
            newUser.acl = ParseACL(parseUser) // set ACL

            newUser.saveInBackground { e ->
                if (e != null) {
                    signupState.postValue(MessageStatus(Status.ERROR, e.message.toString())) // Inform UI
                    Log.e("LOGIN", "Save ACL failed: ${e.message}")
                }
            }

            try {
                UserDao.insert(User(newUser)) // create user in DB
            } catch (e: RealmException) {
                signupState.postValue(MessageStatus(Status.ERROR, e.message.toString())) // Inform UI
                Log.e("LOGIN", "Reaml insert failed: ${e.message}")
                return@withContext false
            }
            true
        }

        if (insertedOk) {
            withContext(Dispatchers.Main) {
                Log.d(
                    "THREAD",
                    "createUser/Scope/repository/withcontext(main) = #${Thread.currentThread().id} // ${Thread.currentThread()}"
                )

                val liveUser: LiveData<User> = UserDao.load(userId = parseUser.objectId)
                if (liveUser.value != null) {
                    currentUser.addSource(liveUser) { value -> currentUser.value = Resource.success(value) }
                    signupState.postValue(MessageStatus(Status.SUCCESS)) // Inform UI
                } else {
                    currentUser.addSource(liveUser) { value ->
                        currentUser.value = Resource.error("Failed to load user with ID ${parseUser.objectId}", value)
                    }
                    signupState.postValue(
                        MessageStatus(
                            Status.ERROR,
                            "Failed to load user with ID ${parseUser.objectId}"
                        )
                    ) // Inform UI
                }
            }
        }
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