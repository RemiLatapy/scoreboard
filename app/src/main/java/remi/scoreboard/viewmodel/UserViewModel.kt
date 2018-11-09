package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
    private val currentUser: LiveData<User> = userRepository.currentUser

    val username: LiveData<String> = Transformations.map(currentUser) { user -> user.username }
    val email: LiveData<String> = Transformations.map(currentUser) { user -> user.email }
    val avatar: LiveData<String> = Transformations.map(currentUser) { user -> user.avatar }
    val isLocal: LiveData<Boolean> = Transformations.map(currentUser) { user -> user.isLocalUser }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun createLocalUser() {
        scope.launch(Dispatchers.IO) { userRepository.insertLocalUser() }
    }
}