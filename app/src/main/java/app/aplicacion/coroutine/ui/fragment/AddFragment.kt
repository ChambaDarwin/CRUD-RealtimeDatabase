package app.aplicacion.coroutine.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.aplicacion.coroutine.R
import app.aplicacion.coroutine.core.hideProgressBar
import app.aplicacion.coroutine.core.showProgressBar
import app.aplicacion.coroutine.core.toast
import app.aplicacion.coroutine.data.model.ImageStorage
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.databinding.FragmentImageBinding
import app.aplicacion.coroutine.ui.viewmodel.UserViewModel
import app.aplicacion.coroutine.util.DataState
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID


class AddFragment : Fragment() {
    private lateinit var binding: FragmentImageBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var refereneStorage: StorageReference
    private val listaUri = mutableListOf<Uri>()
    private lateinit var model: UserViewModel
    private lateinit var imageStorage: ImageStorage
    val lisImages= mutableListOf<String>()
    val lisUid= mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding.btnImageSelected.setOnClickListener {
            selectImages()
        }
        storage = Firebase.storage

        binding.btnRegistrar.setOnClickListener {
            addUser()
        }
        observeInsert()

        return binding.root
    }

    private fun addUser() {
        val nombre = binding.nombre.text.toString()
        val apellido = binding.apellido.text.toString()
        val materia = binding.materia.text.toString()
        val email = binding.email.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                async {
                    getByteArray().forEach {
                        val uid = UUID.randomUUID().toString()
                        launch {
                            val reference =
                                storage.reference.child("User/images/$uid")
                            val result = reference.putBytes(it).await()
                            val images = result.storage.downloadUrl.await().toString()
                            lisImages.add(images)
                            lisUid.add(uid)

                        }

                    }
                }.await()
                imageStorage=ImageStorage(lisUid ,lisImages)

                val user = UserData("", nombre, apellido, email, materia, imageStorage)
                withContext(Dispatchers.Main) {
                    model.inserUser(user)
                }


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    toast("Error: ${e.message}")
                }
            }


        }
    }

    private fun observeInsert() {
        model.insertData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Error -> {
                    binding.progressBar.hideProgressBar()
                }

                is DataState.Loading -> {
                    binding.progressBar.showProgressBar()
                }

                is DataState.Sucess -> {
                    binding.progressBar.hideProgressBar()
                    toast("registro insertado con exito")
                    findNavController().popBackStack()

                }
            }
        })
    }

    private fun selectImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        register.launch(intent)
    }

    val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = it.data
            if (intent?.clipData != null) {
                val count = intent?.clipData?.itemCount ?: 0
                (0 until count).forEach {
                    val imageUri = intent?.clipData?.getItemAt(it)?.uri
                    imageUri?.let {
                        listaUri.add(it)
                    }
                }

            } else {
                val uri = intent?.data
                uri?.let {
                    listaUri.add(uri)
                }

            }
            setSizeOfList()
        }
    }

    private fun setSizeOfList() {
        binding.numeroImagenes.setText("${listaUri.size}")
    }

    fun getByteArray(): List<ByteArray> {
        val lista = mutableListOf<ByteArray>()
        if (!listaUri.isNullOrEmpty()) {

            listaUri.forEach {
                val stream = ByteArrayOutputStream()
                val bitMap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)

                if (bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    lista.add(stream.toByteArray())
                }
            }

        }
        return lista
    }

}