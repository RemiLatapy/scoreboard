package remi.scoreboard.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.R
import remi.scoreboard.adapter.UserListAdapter
import remi.scoreboard.viewmodel.GameViewModel
import remi.scoreboard.viewmodel.UserViewModel

class UserListFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val safeArgs = UserListFragmentArgs.fromBundle(arguments)

        Log.d("TEMP", safeArgs.match.gameId.toString())

        val gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
//        gameViewModel.gameById(safeArgs.match.gameId).observe(this, Observer { game ->
//            game?.let {
//                Log.d(
//                    "DATA_DBG",
//                    "User list fragment got game id = ${safeArgs.match.gameId}, game name = ${game.name}"
//                )
//            }
//        })

        var aa =  gameViewModel.gameById(safeArgs.match.gameId).value
        var bb =  gameViewModel.allGames.value

        return inflater.inflate(R.layout.fragment_player_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        val userListAdapter = UserListAdapter(context!!)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = userListAdapter
    }
}
