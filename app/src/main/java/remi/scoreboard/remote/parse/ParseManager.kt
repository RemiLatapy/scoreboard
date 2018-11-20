package remi.scoreboard.remote.parse

import android.content.Context
import androidx.annotation.WorkerThread
import com.parse.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import remi.scoreboard.R
import remi.scoreboard.data.Game
import remi.scoreboard.data.Player
import remi.scoreboard.data.User

object ParseManager {

    fun init(context: Context) {
        Parse.initialize(
            Parse.Configuration.Builder(context)
                .applicationId(context.getString(R.string.APP_ID))
                .clientKey(context.getString(R.string.CLIENT_KEY))
                .server(context.getString(R.string.PARSE_SERVER_URL))
                .build()
        )
        ParseACL.setDefaultACL(ParseACL(), true)
    }

    // Parse with http interceptor [debug only]
    fun initWithInterceptor(context: Context) {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        Parse.initialize(
            Parse.Configuration.Builder(context)
                .applicationId(context.getString(R.string.APP_ID))
                .clientKey(context.getString(R.string.CLIENT_KEY))
                .server(context.getString(R.string.PARSE_SERVER_URL))
                .clientBuilder(OkHttpClient.Builder().addInterceptor(logger))
                .build()
        )
    }

    // Game
    @WorkerThread
    fun getGameList(): List<Game> {
        val parseGameList = ParseQuery.getQuery<ParseObject>("games").find()
        return parseGameList.map { Game(it) }
    }

    // User & Players
    fun currentUserId(): String? {
        return ParseUser.getCurrentUser()?.objectId
    }

    @WorkerThread
    fun signUpUser(user: User): User {
        user.getParseUser().signUp()
        val parseUser = ParseUser.getCurrentUser()
        parseUser.acl = ParseACL(parseUser)
        parseUser.save()
        return User(parseUser)
    }

    @WorkerThread
    fun logInUser(username: String, password: String): User {
        ParseUser.logIn(username, password)
        val playerList: List<ParseObject> = ParseQuery.getQuery<ParseObject>("player").find()
        return User(ParseUser.getCurrentUser(), playerList)
    }

    @WorkerThread
    fun fetchCurrentUser(): User {
        ParseUser.getCurrentUser().fetch()
        val playerList: List<ParseObject> = ParseQuery.getQuery<ParseObject>("player").find()
        return User(ParseUser.getCurrentUser(), playerList)
    }

    @WorkerThread
    fun resetCurrentUserPassword(email: String) = ParseUser.requestPasswordReset(email)

    @WorkerThread
    fun addPlayerToCurrentUser(player: Player): User {
        val parsePlayer = player.getParsePlayer()
        parsePlayer.acl = ParseUser.getCurrentUser().acl
        parsePlayer.save()
        return fetchCurrentUser()
    }

    @WorkerThread
    fun deleteAllPlayersOfCurrentUser(): User {
        ParseQuery.getQuery<ParseObject>("player").find().forEach { it.delete() }
        return fetchCurrentUser()
    }

    @WorkerThread
    fun deletePlayerOfCurrentUser(id: String): User {
        ParseQuery.getQuery<ParseObject>("player").get(id).delete()
        return fetchCurrentUser()
    }

    @WorkerThread
    fun logOut() = ParseUser.logOut()

    @WorkerThread
    fun renamePlayerOfCurrentUser(id: String, username: String): User {
        val parsePlayer = ParseQuery.getQuery<ParseObject>("player").get(id)
        parsePlayer.put("username", username)
        parsePlayer.save()
        return fetchCurrentUser()
    }
}