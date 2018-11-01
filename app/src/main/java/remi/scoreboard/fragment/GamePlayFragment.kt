package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import remi.scoreboard.R
import remi.scoreboard.adapter.PlayerAdapter
import remi.scoreboard.databinding.FragmentGamePlayBinding
import remi.scoreboard.viewmodel.SharedViewModel

class GamePlayFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity!!.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            sharedViewModel.currentMatch.observe(this, Observer { match ->
                //                Toast.makeText(context, match.game?.name, Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, match.scorePlayerList[0]?.user?.displayName, Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGamePlayBinding.inflate(inflater, container, false)

        val adapter = PlayerAdapter()
        binding.recycler.adapter = adapter
        adapter.submitList(sharedViewModel.currentMatch.value?.scorePlayerList)

        sharedViewModel.currentMatch.observe(this, Observer {
            adapter.notifyDataSetChanged()
            Toast.makeText(context, "Match change (gameplay)", Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.run {
            setImageResource(R.drawable.ic_stop_black_24dp)
            show()
            setOnClickListener { Toast.makeText(context, "FAB Click !", Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gameplay, menu)
    }
}