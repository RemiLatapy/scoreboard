package remi.scoreboard.jetpack.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.R
import remi.scoreboard.jetpack.adapter.GameListAdapter
import remi.scoreboard.jetpack.viewmodel.GameViewModel

class GameListFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        val gameListAdapter = GameListAdapter(context!!)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = gameListAdapter

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        gameViewModel.allGames.observe(this,
                Observer { gameList -> gameList?.let { gameListAdapter.setGameList(it) } })
    }
}
