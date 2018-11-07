package remi.scoreboard.viewmodel

import android.util.Log
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


class SignUpViewModel : ViewModel() {

    private val userRepository: UserRepository = UserRepository()

//    val currentUser: LiveData<Resource<User>> = userRepository.currentUser
    val signupState: LiveData<MessageStatus> = userRepository.signupState

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


    fun createUser(username: String, email: String, password: String) {
        Log.d("THREAD", "createUser = #${Thread.currentThread().id} // ${Thread.currentThread()}")
        scope.launch {
            Log.d("THREAD", "createUser/Scope = #${Thread.currentThread().id} // ${Thread.currentThread()}")
            val user = User(username = username, email = email, password = password)
            userRepository.createUser(user)
        }
    }
}