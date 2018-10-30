package remi.scoreboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import remi.scoreboard.R
import remi.scoreboard.adapter.GameRealmAdapter
import remi.scoreboard.databinding.FragmentGameListBinding
import remi.scoreboard.viewmodel.GameRealmViewModel
//import remi.scoreboard.viewmodel.GameViewModel

class GameListFragment : Fragment() {

//    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameViewModel: GameRealmViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameListBinding.inflate(inflater, container, false)

//        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        gameViewModel = ViewModelProviders.of(this).get(GameRealmViewModel::class.java)

//        val gameListAdapter = GameAdapter()
        val gameListAdapter = GameRealmAdapter()
        binding.recycler.adapter = gameListAdapter
        gameViewModel.allGames.observe(this,
            Observer { gameList -> gameList?.let { gameListAdapter.submitList(it) } })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.hide()
    }
}
