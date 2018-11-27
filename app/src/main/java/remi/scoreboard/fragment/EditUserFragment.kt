package remi.scoreboard.fragment


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
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
import java.io.IOException


class EditUserFragment : Fragment() {

    private val REQUEST_CODE_CAMERA = 0
    private val REQUEST_CODE_GALERY = 1

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
        binding.pickPhotoListener = View.OnClickListener { showPickPhotoDialog() }
        binding.txtDisplayName.setOnFocusChangeListener { v, hasFocus ->
            (v as? EditText)?.let { if (!hasFocus) it.setText(it.text.toString().trim()) }
        }

        binding.setLifecycleOwner(this)
        return binding.root
    }

    private fun showPickPhotoDialog() {
        activity?.also { activity ->

            val bindingDialog = DataBindingUtil.inflate<DialogChoosePhotoSourceBinding>(
                layoutInflater,
                R.layout.dialog_choose_photo_source,
                null,
                false
            )

            val dialog: AlertDialog = AlertDialog.Builder(activity).run {
                setView(bindingDialog.root)
                setTitle(R.string.dialog_choose_photo_source_title)
                show()
            }

            bindingDialog.takePictureListener = View.OnClickListener {

                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(activity.packageManager)?.also {
                        // Create the File where the photo should go
                        val photoFile: File? = try {
                            File.createTempFile("profile_photo", null, activity.cacheDir)
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            // TODO error
                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also { file ->
                            val photoURI: Uri = FileProvider.getUriForFile(
                                activity,
                                "com.remi.scoreboard.fileprovider",
                                file
                            )
                            viewmodel.photoPath = photoFile.absolutePath
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
                            dialog.dismiss()
                        }
                    }
                }
            }

            bindingDialog.selectFromGalleryListener = View.OnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                startActivityForResult(intent, REQUEST_CODE_GALERY)
                dialog.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAMERA -> {
                    viewmodel.currentUser.avatar = viewmodel.photoPath ?: viewmodel.currentUser.avatar
                    binding.invalidateAll()
                }
                REQUEST_CODE_GALERY -> {
                    data?.data?.also { uri ->
                        context?.also { ctx ->
                            val file = try {
                                File.createTempFile("profile_photo", null, ctx.cacheDir)
                            } catch (ex: IOException) {
                                // Error occurred while creating the File
                                // TODO error
                                null
                            }
                            file?.also {
                                FileUtil.streamUriToFile(ctx, uri, file)
                                viewmodel.currentUser.avatar = file.absolutePath
                                binding.invalidateAll()
                            }
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
