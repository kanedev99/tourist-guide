package vinova.kane.touristguide.ui.profile

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentEditProfileBinding
import vinova.kane.touristguide.firebase.FirebaseViewModel
import vinova.kane.touristguide.utils.SaveSharedPreference
import vinova.kane.touristguide.vo.User
import java.util.*


class EditProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    lateinit var binding: FragmentEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    private val firebaseViewModel: FirebaseViewModel by viewModel()
    private lateinit var userLiveData: LiveData<DataSnapshot>

    private var user: User? = User()

    private lateinit var storageReference: StorageReference
    private var filePath: Uri? = null


    companion object {
        const val PICK_IMAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().getReference("users").child(auth.uid!!)

        userLiveData = firebaseViewModel.getUserLiveData()!!

        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(inflater)

        binding.fragment = this

        getImageFromStorage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLiveData.observe(viewLifecycleOwner,
            Observer<DataSnapshot> { dataSnapShot ->
                user = dataSnapShot.child(auth.uid!!).getValue(User::class.java)
                with(binding) {
                    edtUsername.setText(user?.name)
                    edtEmail.setText(user?.email)
                    edtPhone.setText(user?.phone)
                    edtDateOfBirth.setText(user?.dateOfBirth)
                }
            })
    }

    fun initEvent(view: View) {
        when (view) {
            binding.ivBack -> findNavController().popBackStack()

            binding.tvSave -> {
                dbReference.child("name").setValue(binding.edtUsername.text.toString())
                dbReference.child("phone").setValue(binding.edtPhone.text.toString())
                dbReference.child("dateOfBirth").setValue(binding.edtDateOfBirth.text.toString())

                SaveSharedPreference().setUsername(requireContext(), binding.edtUsername.text.toString())

                Toast.makeText(requireContext(), "Update Profile Successful", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.edtDateOfBirth -> {
                val now: Calendar = Calendar.getInstance()
                val dpd: DatePickerDialog = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),  // Initial year selection
                    now.get(Calendar.MONTH),  // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Initial day selection
                )
                dpd.show(childFragmentManager, "Datepickerdialog")
            }

            binding.ivChangePhoto -> {
                getImageFromAlbum()
            }
        }
    }

    private fun getImageFromAlbum() {
        try {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, PICK_IMAGE)
        } catch (e: ActivityNotFoundException) {
            print(e)
        }
    }

    private fun getImageFromStorage() {
        val imgUri = SaveSharedPreference().getImageUri(requireContext())
        if (imgUri?.isNotEmpty()!!){
            Glide.with(requireContext())
                    .load(imgUri)
                    .into(binding.ivAvatar)
        } else {
            binding.ivAvatar.setImageResource(R.drawable.avatar_default)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            filePath = data?.data!!
            binding.ivAvatar.setImageURI(filePath)
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference.child("images/" + auth.uid)
            val uploadTask = ref.putFile(filePath!!)

            val urlTask =
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        addUploadRecordToDb(downloadUri.toString())
                        SaveSharedPreference().setImageUri(requireContext(), downloadUri.toString())
                    } else {
                        Timber.e(task.exception)
                    }
                }.addOnFailureListener {

                }
        } else {
            Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUploadRecordToDb(uri: String) {
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("posts")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(activity, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        binding.edtDateOfBirth.setText(
            getString(
                R.string.day_of_birth_select,
                dayOfMonth,
                monthOfYear,
                year
            )
        )
    }


}