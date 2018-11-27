package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
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

    val currentUser: User = userRepository.unmanageCurrentUser

    val editUserState: LiveData<MessageStatus> = userRepository.editUserState

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun editCurrentUser() {
        scope.launch(Dispatchers.IO) {
            userRepository.editCurrentUser(currentUser)
        }
    }

//    class EditUserViewModelFactory(private val userId: String) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return EditUserViewModel(userId) as T
//        }
//    }
}



