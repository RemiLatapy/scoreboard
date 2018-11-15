package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.Player
import remi.scoreboard.data.PlayerList
import remi.scoreboard.data.User
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    //    val currentUser: LiveData<User> = userRepository.loadUser(currentUserId)
    val currentUser: LiveData<User> = userRepository.currentUser

    val addPlayerState: LiveData<MessageStatus> = userRepository.addPlayerState
    val deleteAllPlayerState: LiveData<MessageStatus> = userRepository.deleteAllPlayerState
    val deletePlayerState: LiveData<MessageStatus> = userRepository.deletePlayerState
    val renamePlayerState: LiveData<MessageStatus> = userRepository.renamePlayerState
    val signOutState: LiveData<MessageStatus> = userRepository.signOutState

    val userId: LiveData<String> = Transformations.map(currentUser) { user -> user.id }
    val username: LiveData<String> = Transformations.map(currentUser) { user -> user.username }
    val email: LiveData<String> = Transformations.map(currentUser) { user -> user.email }
    val avatar: LiveData<String> = Transformations.map(currentUser) { user -> user.avatar }
    val isLocal: LiveData<Boolean> = Transformations.map(currentUser) { user -> user.isLocalUser }
    val playerListSize: LiveData<Int> = Transformations.map(currentUser) { user -> user.playerList.size }
    val playerList: LiveData<PlayerList> = Transformations.map(currentUser) { user -> user.playerList }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun createLocalUser() {
        scope.launch(Dispatchers.IO) { userRepository.insertLocalUser() }
    }

    fun addPlayer(username: String) {
        val player = Player(username = username)
        scope.launch(Dispatchers.IO) { userRepository.addPlayerToCurrentUser(player) }
    }

    fun deleteAllPlayer() {
        scope.launch(Dispatchers.IO) { userRepository.deleteAllPlayerOfCurrentUser() }
    }

    fun signOut() {
        scope.launch(Dispatchers.IO) { userRepository.signOut() }
    }

    fun deletePlayer(playerId: String) {
        scope.launch(Dispatchers.IO) { userRepository.deletePlayerOfCurrentUser(playerId) }
    }

    fun renamePlayer(playerId: String, newPlayerName: String) {
        scope.launch(Dispatchers.IO) { userRepository.renamePlayerOfCurrentUser(playerId, newPlayerName) }
    }
}