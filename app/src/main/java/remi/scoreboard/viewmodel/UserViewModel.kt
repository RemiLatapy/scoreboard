package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.ScoreboardDatabase
import remi.scoreboard.data.User
import remi.scoreboard.repository.UserRepository
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