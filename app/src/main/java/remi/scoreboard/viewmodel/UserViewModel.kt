package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.User
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository()
    val allUsers = repository.allUsers

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(user: User) = scope.launch(Dispatchers.IO) { repository.insert(user) }

    fun deleteAll() = scope.launch(Dispatchers.IO) { repository.deleteAll() }
}