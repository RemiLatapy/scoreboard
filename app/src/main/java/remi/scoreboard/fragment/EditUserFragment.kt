package remi.scoreboard.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentEditUserBinding
import remi.scoreboard.viewmodel.EditUserViewModel

class EditUserFragment : Fragment() {

    private lateinit var viewmodel: EditUserViewModel
    private lateinit var binding: FragmentEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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
        binding.saveUserListener = saveUserClickListener


        binding.txtDisplayName.setOnFocusChangeListener { v, hasFocus ->
            (v as? EditText)?.let { if (!hasFocus) it.setText(it.text.toString().trim()) }
        }


        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    private val saveUserClickListener = View.OnClickListener {
        viewmodel.editCurrentUser(binding.txtDisplayName.text.toString())
    }
}
