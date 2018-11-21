package remi.scoreboard.remote.parse

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import com.parse.*
import com.parse.ParseException.OTHER_CAUSE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import remi.scoreboard.R
import remi.scoreboard.data.Game
import remi.scoreboard.data.Match
import remi.scoreboard.data.Player
import remi.scoreboard.data.User

object ParseManager {

    private lateinit var connectivityManager: ConnectivityManager

    fun init(context: Context) {
        Parse.initialize(
            Parse.Configuration.Builder(context)
                .applicationId(context.getString(R.string.APP_ID))
                .clientKey(context.getString(R.string.CLIENT_KEY))
                .server(context.getString(R.string.PARSE_SERVER_URL))
                .build()
        )

        connectivityManager = context.getSystemService(ConnectivityManager::class.java)
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

        connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    }

    /**
     * @throws ParseException no network
     */
    private fun ensureConnection() {
        if (connectivityManager.activeNetworkInfo?.isConnected != true)
            throw ParseException(OTHER_CAUSE, "No network")
    }

    // Game
    @WorkerThread
    fun getGameList(): List<Game> {
        ensureConnection()
        val parseGameList = ParseQuery.getQuery<ParseObject>("games").find()
        return parseGameList.map { Game(it) }
    }

    // User & Players
    fun currentUserId(): String? {
        return ParseUser.getCurrentUser()?.objectId
    }

    @WorkerThread
    fun signUpUser(user: User): User {
        ensureConnection()
        user.getParseUser().signUp()
        val parseUser = ParseUser.getCurrentUser()
        parseUser.acl = ParseACL(parseUser)
        parseUser.save()
        return User(parseUser)
    }

    @WorkerThread
    fun logInUser(username: String, password: String): User {
        ensureConnection()
        ParseUser.logIn(username, password)
        val playerList: List<ParseObject> = ParseQuery.getQuery<ParseObject>("player").find()
        return User(ParseUser.getCurrentUser(), playerList)
    }

    @WorkerThread
    fun fetchCurrentUser(): User {
        ensureConnection()
        ParseUser.getCurrentUser().fetch()
        val playerList: List<ParseObject> = ParseQuery.getQuery<ParseObject>("player").find()
        return User(ParseUser.getCurrentUser(), playerList)
    }

    @WorkerThread
    fun resetCurrentUserPassword(email: String) {
        ensureConnection()
        ParseUser.requestPasswordReset(email)
    }

    @WorkerThread
    fun addPlayerToCurrentUser(player: Player): User {
        ensureConnection()
        val parsePlayer = player.getParsePlayer()
        parsePlayer.acl = ParseUser.getCurrentUser().acl
        parsePlayer.save()
        return fetchCurrentUser()
    }

    @WorkerThread
    fun deleteAllPlayersOfCurrentUser(): User {
        ensureConnection()
        ParseQuery.getQuery<ParseObject>("player").find().forEach { it.delete() }
        return fetchCurrentUser()
    }

    @WorkerThread
    fun deletePlayerOfCurrentUser(id: String): User {
        ensureConnection()
        ParseQuery.getQuery<ParseObject>("player").get(id).delete()
        return fetchCurrentUser()
    }

    @WorkerThread
    fun logOut() {
        ensureConnection()
        ParseUser.logOut()
    }

    @WorkerThread
    fun renamePlayerOfCurrentUser(id: String, username: String): User {
        ensureConnection()
        val parsePlayer = ParseQuery.getQuery<ParseObject>("player").get(id)
        parsePlayer.put("username", username)
        parsePlayer.save()
        return fetchCurrentUser()
    }

    // Matches
    @WorkerThread
    fun createMatch(match: Match): Match {
        ensureConnection()

        // Create & save player score
        val parsePlayerScore = match.scorePlayerList.map { it.getParsePlayerScore() }.apply {
            forEach { it.acl = ParseUser.getCurrentUser().acl }
        }
        ParseObject.saveAll(parsePlayerScore)

        // Create & save match
        val parseMatch = match.getParseMatchWithParsePlayerScores(parsePlayerScore)
        parseMatch.acl = ParseUser.getCurrentUser().acl
        parseMatch.save()

        return Match(parseMatch)
    }
}