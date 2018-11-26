package remi.scoreboard.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.R
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.Player
import remi.scoreboard.data.PlayerList
import remi.scoreboard.data.User
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    val currentUser: LiveData<User> = userRepository.currentUser

    val addPlayerState: LiveData<MessageStatus> = userRepository.addPlayerState
    val deleteAllPlayerState: LiveData<MessageStatus> = userRepository.deleteAllPlayerState
    val deletePlayerState: LiveData<MessageStatus> = userRepository.deletePlayerState
    val renamePlayerState: LiveData<MessageStatus> = userRepository.renamePlayerState
    val logOutState: LiveData<MessageStatus> = userRepository.logOutState
    val updateUserState: LiveData<MessageStatus> = userRepository.updateUserState

    val userId: LiveData<String> = Transformations.map(currentUser) { user -> user.id }
    val username: LiveData<String> = Transformations.map(currentUser) { user -> user.username }
    val email: LiveData<String> = Transformations.map(currentUser) { user -> user.email }
    val avatar: LiveData<String> = Transformations.map(currentUser) { user -> user.avatar }
    val isLocal: LiveData<Boolean> = Transformations.map(currentUser) { user -> user.isLocalUser }
    val playerList: LiveData<PlayerList> = Transformations.map(currentUser) { user -> user.playerList }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun addPlayer(username: String, activity: FragmentActivity?) {
        val player = Player(username = username)

        // TODO temp random avatar
        val avatarArray = activity?.resources?.getStringArray(R.array.avatars)
        avatarArray?.let {
            val rand = Random.nextInt(0, it.size)
            player.avatar = avatarArray[rand]
        }

        scope.launch(Dispatchers.IO) { userRepository.addPlayerToCurrentUser(player) }
    }

    fun deleteAllPlayers() {
        scope.launch(Dispatchers.IO) { userRepository.deleteAllPlayersOfCurrentUser() }
    }

    fun logOut() {
        scope.launch(Dispatchers.IO) { userRepository.logOut() }
    }

    fun deletePlayer(playerId: String) {
        scope.launch(Dispatchers.IO) { userRepository.deletePlayerOfCurrentUser(playerId) }
    }

    fun renamePlayer(playerId: String, newPlayerName: String) {
        scope.launch(Dispatchers.IO) { userRepository.renamePlayerOfCurrentUser(playerId, newPlayerName) }
    }

    fun refreshUser() {
        scope.launch(Dispatchers.IO) { userRepository.refreshCurrentUser() }
    }
}