package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.User
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class EditUserViewModel() : ViewModel() {
    private val userRepository = UserRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    private val currentUser: LiveData<User> = userRepository.currentUser
    val displayName: LiveData<String> = Transformations.map(currentUser) { user -> user.displayName }

    val editUserState: LiveData<MessageStatus> = userRepository.editUserState

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun editCurrentUser(displayName: String) {
        scope.launch(Dispatchers.IO) {
            userRepository.editCurrentUser(displayName = displayName)
        }
    }

//    class EditUserViewModelFactory(private val userId: String) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return EditUserViewModel(userId) as T
//        }
//    }
}



