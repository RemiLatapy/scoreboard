package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.*
import remi.scoreboard.repository.GameRepository
import remi.scoreboard.repository.MatchRepository
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class ChoosePlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository()
    private val matchRepository = MatchRepository()
    private val gameRepository = GameRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    // TODO is it possible & better to observe playerlist of current user instead of whole user?
    val currentUser: LiveData<User> = userRepository.currentUser

    val updateUserState: LiveData<MessageStatus> = userRepository.updateUserState
    val createMatchState: LiveData<MessageStatus> = matchRepository.createMatchState

    fun getGameById(id: String): LiveData<Game> = gameRepository.getGameById(id)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun refreshUser() {
        scope.launch(Dispatchers.IO) { userRepository.refreshCurrentUser() }
    }

    fun createMatch(game: Game, playerList: List<Player>) {
        Realm.getDefaultInstance().use {
            val unmanagedPlayerList = it.copyFromRealm(playerList)
            val unmanagedGame = it.copyFromRealm(game)

            val match = Match(unmanagedGame, unmanagedPlayerList)
            scope.launch(Dispatchers.IO) { matchRepository.create(match) }
        }

    }
}