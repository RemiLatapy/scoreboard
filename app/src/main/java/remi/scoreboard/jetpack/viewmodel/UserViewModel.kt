package remi.scoreboard.jetpack.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.jetpack.model.ScoreboardDatabase
import remi.scoreboard.jetpack.model.User
import remi.scoreboard.jetpack.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    val allUsers: LiveData<List<User>>

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        val userDao = ScoreboardDatabase.getDatabase(application, scope).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(user: User) = scope.launch(Dispatchers.IO) { repository.insert(user) }
}