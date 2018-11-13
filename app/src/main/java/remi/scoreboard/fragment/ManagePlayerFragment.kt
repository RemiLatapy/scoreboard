package remi.scoreboard.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import remi.scoreboard.R
import remi.scoreboard.adapter.PlayerAdapter
import remi.scoreboard.databinding.FragmentManagePlayerBinding
import remi.scoreboard.viewmodel.UserViewModel

class ManagePlayerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
//        userViewModel.playerList.observe(this, Observer {
//            Toast.makeText(context, "Player added", Toast.LENGTH_SHORT).show()
//        })
//        userViewModel.playerListIsEmpty.observe(this, Observer {
//            Toast.makeText(context, "Player is empty $it", Toast.LENGTH_SHORT).show()
//        })


        val userId = activity?.let {
            ManagePlayerFragmentArgs.fromBundle(it.intent.extras).userId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentManagePlayerBinding.inflate(inflater, container, false)
        binding.addPlayerListener = View.OnClickListener { showAddPlayerDialog() }

        userViewModel.currentUser.observe(this, Observer { user ->
            binding.playerList = user.playerList
            Log.d("PLAYER", "User update: $user")
        })

        val playerListAdapter = PlayerAdapter()
        binding.recycler.adapter = playerListAdapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_add -> {
                showAddPlayerDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddPlayerDialog() {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.let {
            val view = layoutInflater.inflate(R.layout.dialog_new_user, null)
            it.setTitle(R.string.create_user)
                .setView(view)
                .setPositiveButton(
                    "Add"
                ) { _, _ ->
                    // TODO insert player in user player list here
//                            val firstName = view.findViewById<TextInputEditText>(R.id.first_name)
//                            val lastName = view.findViewById<TextInputEditText>(R.id.last_name)
//                            userViewModel.insert(User(firstName.text.toString(), lastName.text.toString()))
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
