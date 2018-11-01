package remi.scoreboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import remi.scoreboard.R
import remi.scoreboard.adapter.GameAdapter
import remi.scoreboard.data.Game
import remi.scoreboard.databinding.FragmentGameListBinding
import remi.scoreboard.viewmodel.GameViewModel
import remi.scoreboard.viewmodel.SharedViewModel

class GameListFragment : Fragment(), GameAdapter.GameSelectedCallback {

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameListBinding.inflate(inflater, container, false)

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        val gameListAdapter = GameAdapter(this)
        binding.recycler.adapter = gameListAdapter
        gameViewModel.allGames.observe(this,
            Observer { gameList -> gameList?.let { gameListAdapter.submitList(it) } })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(findNavController())

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.hide()
    }

    // TODO delete match if not started
    override fun onGameSelected(game: Game) {
        activity?.let {
            ViewModelProviders.of(it).get(SharedViewModel::class.java).createMatch(game)
        }
        val action = GameListFragmentDirections.nextAction()
        findNavController().navigate(action)
    }
}

