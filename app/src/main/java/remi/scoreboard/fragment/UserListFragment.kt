package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import remi.scoreboard.R
import remi.scoreboard.adapter.PlayerAdapter
import remi.scoreboard.databinding.FragmentUserListBinding
import remi.scoreboard.viewmodel.UserViewModel

class UserListFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val gameId = activity?.let {
            UserListFragmentArgs.fromBundle(it.intent.extras).gameId
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUserListBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val userListAdapter = PlayerAdapter()
        binding.recycler.adapter = userListAdapter
        // https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-reyclerview
        // TODO user -> player
//        userListAdapter.submitList(userViewModel.allUsers.value)
//
//        userViewModel.allUsers.observe(this,
//            Observer { userListAdapter.notifyDataSetChanged() })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_player -> {
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


//                userViewModel.insert(
//                    User(
//                        firstName = "user ",
//                        lastName = (0..100).random().toString(),
//                        email = (0..100).random().toString() + "@" + (0..100).random().toString() + ".com"
//                    )
//                )
                true
            }
            R.id.action_delete_all_player -> {
//                userViewModel.deleteAll()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}

