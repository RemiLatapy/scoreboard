package remi.scoreboard.fragment


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import remi.scoreboard.R
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.DialogChoosePhotoSourceBinding
import remi.scoreboard.databinding.FragmentEditUserBinding
import remi.scoreboard.util.FileUtil
import remi.scoreboard.viewmodel.EditUserViewModel
import java.io.File
import java.util.*


class EditUserFragment : Fragment() {

    private lateinit var viewmodel: EditUserViewModel
    private lateinit var binding: FragmentEditUserBinding

    private var imageUri: Uri? = null

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
        binding.pickPhotoListener = View.OnClickListener { showPickPhotoDialog() }
        binding.txtDisplayName.setOnFocusChangeListener { v, hasFocus ->
            (v as? EditText)?.let { if (!hasFocus) it.setText(it.text.toString().trim()) }
        }


        binding.setLifecycleOwner(this)
        return binding.root
    }

    private fun showPickPhotoDialog() {
        activity?.let { activity ->

            val bindingDialog = DataBindingUtil.inflate<DialogChoosePhotoSourceBinding>(
                layoutInflater,
                R.layout.dialog_choose_photo_source,
                null,
                false
            )

            bindingDialog.takePictureListener = View.OnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photo = File(Environment.getExternalStorageDirectory(), "profile_${Date().time}.jpg")
                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photo)
                )
                imageUri = Uri.fromFile(photo)
                startActivityForResult(intent, 0)
            }

            bindingDialog.selectFromGalleryListener = View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                startActivityForResult(intent, 1)
            }

            AlertDialog.Builder(activity).apply {
                setView(bindingDialog.root)
                setTitle(R.string.dialog_choose_photo_source_title)
                show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0 -> viewmodel.currentUser.avatar = imageUri?.path ?: viewmodel.currentUser.avatar
                1 -> {
                    data?.data?.let { uri ->
                        context?.let { ctx ->
                            val file = File.createTempFile("profile_photo", null, ctx.cacheDir)
                            FileUtil.streamUriToFile(ctx, uri, file)
                            viewmodel.currentUser.avatar = file.absolutePath
                            binding.invalidateAll()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_save_user -> {
                viewmodel.editCurrentUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
