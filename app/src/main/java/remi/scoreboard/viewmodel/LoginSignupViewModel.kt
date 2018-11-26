package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.Game
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.User
import remi.scoreboard.repository.GameRepository
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext


class LoginSignupViewModel : ViewModel() {

    private val userRepository: UserRepository = UserRepository()
    private val gameRepository: GameRepository by lazy { GameRepository() }

    val signUpState: LiveData<MessageStatus> = userRepository.signUpState
    val logInState: LiveData<MessageStatus> = userRepository.logInState
    val resetPasswordState: LiveData<MessageStatus> = userRepository.resetPasswordState

    val updateGameListState: LiveData<MessageStatus> = gameRepository.updateGameListState

    val allGames: LiveData<List<Game>> by lazy { gameRepository.allGames }


    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


    fun signUpUser(username: String, email: String, password: String) {
        scope.launch(Dispatchers.IO) {
            val user = User(username = username, email = email, password = password)
            userRepository.signUpUser(user)
        }
    }

    fun loginUser(username: String, password: String) {
        scope.launch(Dispatchers.IO) {
            userRepository.loginUser(username = username,password =  password)
        }
    }

    fun resetPassword(email: String) {
        scope.launch(Dispatchers.IO) {
            userRepository.resetPassword(email)
        }
    }

    fun updateGameList()  {
        scope.launch {
            gameRepository.updateGameList()
        }
    }
}