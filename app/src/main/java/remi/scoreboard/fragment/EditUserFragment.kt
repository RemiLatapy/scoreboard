package remi.scoreboard.fragment


import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import remi.scoreboard.R
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentEditUserBinding
import remi.scoreboard.viewmodel.EditUserViewModel

class EditUserFragment : Fragment() {

    private lateinit var viewmodel: EditUserViewModel
    private lateinit var binding: FragmentEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        viewmodel = ViewModelProviders.of(this).get(EditUserViewModel::class.java)
        viewmodel.editUserState.observe(this, Observer { cb ->
            when (cb.status) {
                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.txtError.text = ""
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.INVISIBLE
                    binding.txtError.text = cb.message
                }
                Status.SUCCESS -> activity?.finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)
        binding.viewModel = viewmodel

        binding.txtDisplayName.setOnFocusChangeListener { v, hasFocus ->
            (v as? EditText)?.let { if (!hasFocus) it.setText(it.text.toString().trim()) }
        }


        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_save_user -> {
                viewmodel.editCurrentUser(binding.txtDisplayName.text.toString())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
