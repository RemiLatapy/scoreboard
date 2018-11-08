package remi.scoreboard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class MainEmptyViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun createLocalUser() {
        scope.launch(Dispatchers.IO) { userRepository.insertLocalUser() }
    }
}