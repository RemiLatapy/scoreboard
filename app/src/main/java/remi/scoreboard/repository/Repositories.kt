package remi.scoreboard.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.parse.*
import io.realm.exceptions.RealmException
import io.realm.exceptions.RealmMigrationNeededException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import remi.scoreboard.data.*


class UserRepository {

    // callbacks
    val signupState = MutableLiveData<MessageStatus>()
    val loginState = MutableLiveData<MessageStatus>()
    val resetPasswordState = MutableLiveData<MessageStatus>()
    val addPlayerState = MutableLiveData<MessageStatus>()
    val deleteAllPlayerState = MutableLiveData<MessageStatus>()
    val signOutState = MutableLiveData<MessageStatus>()

    private val currentUserId = ParseUser.getCurrentUser()?.objectId ?: "0"
    val currentUser = UserDao.load(currentUserId)

    @WorkerThread
    suspend fun refreshCurrentUser(user: ParseUser) {
        user.fetch()
        UserDao.update(User(ParseUser()))
    }

    @WorkerThread
    suspend fun insert(user: User) = UserDao.insert(user)

    @WorkerThread
    suspend fun insertOrUpdate(user: User) = UserDao.insertOrUpdate(user)

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
                    signupState.postValue(MessageStatus(Status.SUCCESS)) // Inform UI
                } else {
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

    @WorkerThread
    suspend fun loginUser(username: String, password: String) {
        loginState.postValue(MessageStatus(Status.LOADING))

        try {
            ParseUser.logIn(username, password)
            val playerList: List<ParseObject> = ParseQuery.getQuery<ParseObject>("player").find()
            UserDao.insertOrUpdate(User(ParseUser.getCurrentUser(), playerList))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalStateException -> {
                    loginState.postValue(MessageStatus(Status.ERROR, e.message.toString()))
                }
                else -> throw e
            }
        }

        loginState.postValue(MessageStatus(Status.SUCCESS))
    }

    @WorkerThread
    suspend fun resetPassword(email: String) {
        resetPasswordState.postValue(MessageStatus(Status.LOADING))

        withContext(Dispatchers.IO) {
            try {
                ParseUser.requestPasswordReset(email)
                resetPasswordState.postValue(MessageStatus(Status.SUCCESS, "Reset password email sent"))
            } catch (e: ParseException) {
                resetPasswordState.postValue(MessageStatus(Status.ERROR, e.message.toString()))
            }
        }
    }

    fun loadLocalUser(): LiveData<User> {
        return UserDao.load("0")
    }

    @WorkerThread
    suspend fun insertLocalUser() {
        UserDao.insert(User(isLocalUser = true, id = "0"))
    }

    fun loadUser(currentUserId: String): LiveData<User> = UserDao.load(currentUserId)

    @WorkerThread
    suspend fun addPlayerToCurrentUser(player: Player) {
        addPlayerState.postValue(MessageStatus(Status.LOADING))

        val parsePlayer = player.getParsePlayer()
        parsePlayer.acl = ParseUser.getCurrentUser().acl
        try {
            parsePlayer.save()
            val savedPlayer = Player(parsePlayer)
            UserDao.addPlayerToUser(savedPlayer, ParseUser.getCurrentUser().objectId)
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException -> {
                    addPlayerState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                    return
                }
                else -> throw e
            }
        }

        addPlayerState.postValue(MessageStatus(Status.SUCCESS))
    }

    @WorkerThread
    suspend fun deleteAllPlayerOfCurrentUser() {
        deleteAllPlayerState.postValue(MessageStatus(Status.LOADING))
        try {
            ParseQuery.getQuery<ParseObject>("player").find().forEach { it.delete() }
            UserDao.deleteAllPlayerOfUser(ParseUser.getCurrentUser().objectId)
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException -> {
                    deleteAllPlayerState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                    return
                }
                else -> throw e
            }
        }

        deleteAllPlayerState.postValue(MessageStatus(Status.SUCCESS))
    }

    @WorkerThread
    suspend fun signOut() {
        signOutState.postValue(MessageStatus(Status.LOADING))
        try {
            ParseUser.logOut()
        } catch (e: ParseException) {
            signOutState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
        }

        signOutState.postValue(MessageStatus(Status.SUCCESS))
    }
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

    val updateGameListState = MutableLiveData<MessageStatus>()
    @WorkerThread
    suspend fun updateGameList() {
        updateGameListState.postValue(MessageStatus(Status.LOADING))
        val query = ParseQuery.getQuery<ParseObject>("games")
        try {
            val parseGameList = query.find()
            GameDao.insertOrUpdate(parseGameList.map { Game(it) })
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException, is RealmMigrationNeededException -> {
                    updateGameListState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                    return
                }
                else -> throw e
            }
        }

        updateGameListState.postValue(MessageStatus(Status.SUCCESS))
    }

    @WorkerThread
    suspend fun insert(game: Game) = GameDao.insert(game)

    @WorkerThread
    suspend fun insert(games: List<Game>) = GameDao.insert(games)

    @WorkerThread
    suspend fun deleteAll() = GameDao.deleteAll()

}
