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
import app.aplicacion.coroutine.databinding.FragmentAddBinding

import app.aplicacion.coroutine.ui.viewmodel.UserViewModel
import app.aplicacion.coroutine.util.DataState
import app.aplicacion.coroutine.util.ValidateField
import app.aplicacion.coroutine.util.ValidateUser
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var referenceStorage: StorageReference
    private val listaUri = mutableListOf<Uri>()
    private lateinit var model: UserViewModel
    private lateinit var imageStorage: ImageStorage
    private val lisImages = mutableListOf<String>()
    private val lisUid = mutableListOf<String>()
    private val lista = mutableListOf<ByteArray>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding.btnImageSelected.setOnClickListener {
            selectImages()
        }
        storage = Firebase.storage

        binding.btnRegistrar.setOnClickListener {
            saveImages()
        }
        observeInsertStorage()
        observeValidationErrors()

        return binding.root
    }

    private fun observeValidationErrors() {
        lifecycleScope.launchWhenStarted {
            model.validateUser.collect {
                if (it.nombre is ValidateField.Error) {
                    binding.nombre.apply {
                        requestFocus()
                        setError(it.nombre.message)
                    }
                }
                if (it.apellido is ValidateField.Error) {
                    binding.apellido.apply {
                        requestFocus()
                        setError(it.apellido.message)
                    }
                }
                if (it.materia is ValidateField.Error) {
                    binding.materia.apply {
                        requestFocus()
                        setError(it.materia.message)
                    }
                }
                if (it.email is ValidateField.Error) {
                    binding.email.apply {
                        requestFocus()
                        setError(it.email.message)
                    }
                }
            }
        }
    }

    private fun saveImages() {
        val byteArrayList = getByteArray()
        if (byteArrayList.isNotEmpty()) {
            model.insertImage(byteArrayList)
            listaUri.clear()
        } else {
            toast("La lista byteArray está vacía")
        }
    }

    private fun observeInsertStorage() {
        model.storeCloud.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Error -> {
                    binding.progressBar.hideProgressBar()
                    toast(it.message.toString())
                }
                is DataState.Loading -> binding.progressBar.showProgressBar()
                is DataState.Sucess -> {
                    binding.progressBar.hideProgressBar()
                    val nombre = binding.nombre.text.toString()
                    val apellido = binding.apellido.text.toString()
                    val materia = binding.materia.text.toString()
                    val email = binding.email.text.toString()
                    if (nombre.isNotEmpty() && apellido.isNotEmpty() && materia.isNotEmpty() && email.isNotEmpty()) {
                        val user = UserData("", nombre, apellido, email, materia, it.data!!)
                        model.insertUser(user)
                        toast("Registro realizado con éxito")
                        findNavController().popBackStack(R.id.mainFragment, false)
                    }
                }
            }
        })
    }

    private fun selectImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            type = "image/*"
        }
        register.launch(intent)
    }

    private val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = it.data
            if (intent?.clipData != null) {
                val count = intent.clipData?.itemCount ?: 0
                for (i in 0 until count) {
                    intent.clipData?.getItemAt(i)?.uri?.let { uri ->
                        listaUri.add(uri)
                    }
                }
            } else {
                intent?.data?.let { uri ->
                    listaUri.add(uri)
                }
            }
            setSizeOfList()
        }
    }

    private fun setSizeOfList() {
        binding.numeroImagenes.text = "${listaUri.size}"
    }

    private fun getByteArray(): List<ByteArray> {
        lista.clear()
        if (listaUri.isNotEmpty()) {
            listaUri.forEach { uri ->
                val stream = ByteArrayOutputStream()
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    lista.add(stream.toByteArray())
                }
            }
        }
        return lista
    }
}
