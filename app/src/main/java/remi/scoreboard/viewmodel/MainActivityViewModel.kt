package remi.scoreboard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.repository.MatchRepository
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel() : ViewModel() {
    private val matchRepository = MatchRepository()
    private val userRepository = UserRepository()

    val tempMatch = matchRepository.tempMatch

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun refreshUser() {
        scope.launch(Dispatchers.IO) {
            userRepository.refreshCurrentUser() // Need to fetch user to get it ACL
        }
    }

    fun saveAndDeleteTempMatch() {
        scope.launch(Dispatchers.IO) {
            userRepository.refreshCurrentUser() // Need to fetch user to get it ACL
            matchRepository.saveLocalMatch()
            matchRepository.deleteLocalMatch()
        }
    }
}