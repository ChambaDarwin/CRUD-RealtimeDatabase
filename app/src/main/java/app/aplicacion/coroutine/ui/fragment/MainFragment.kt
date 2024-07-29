package app.aplicacion.coroutine.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.coroutine.ui.adapter.DeleteUser
import app.aplicacion.coroutine.R
import app.aplicacion.coroutine.core.hideProgressBar
import app.aplicacion.coroutine.core.showProgressBar
import app.aplicacion.coroutine.core.toast
import app.aplicacion.coroutine.ui.adapter.UserAdapter
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.databinding.FragmentMainBinding
import app.aplicacion.coroutine.ui.viewmodel.UserViewModel
import app.aplicacion.coroutine.util.DataState
import app.aplicacion.coroutine.util.ValidateField
import app.aplicacion.coroutine.util.validateEmail
import app.aplicacion.coroutine.util.validateName
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var model: UserViewModel
    private lateinit var cadapter: UserAdapter
    val listaUri = mutableListOf<Uri>()
    private lateinit var mostrar: TextView
    private lateinit var storage: FirebaseStorage
    private lateinit var progress: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding.btnAddUser.setOnClickListener {
           findNavController().navigate(R.id.action_mainFragment_to_addFragment)
        }
        storage = Firebase.storage
        initRecycler()
        showData()
        enviarDatos()
        deleteUser()




        return binding.root
    }

    private fun deleteUser() {
        val delete = object : DeleteUser() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posiiton = viewHolder.adapterPosition
                val user = cadapter.diff.currentList[posiiton]
                model.deleteUser(user)
                observeDelete()

            }

        }
        ItemTouchHelper(delete).attachToRecyclerView(binding.recycler)
    }

private fun observeDelete(){
    model.observeDelete.observe(viewLifecycleOwner, Observer {
        when(it){
            is DataState.Error -> {
                binding.progressBar.hideProgressBar()
                toast(it.message.toString())
            }
            is DataState.Loading -> binding.progressBar.showProgressBar()
            is DataState.Sucess -> {

                binding.progressBar.hideProgressBar()
                toast(it.data.toString())
            }
        }
    })
}

    private fun enviarDatos() {
        cadapter.sendId {
            val bundle = Bundle().apply {
                putSerializable("user", it)
            }
            findNavController().navigate(R.id.action_mainFragment_to_ediFragment, bundle)
        }

    }





    private fun initRecycler() {
        cadapter = UserAdapter()
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cadapter
            setHasFixedSize(true)
        }
    }

    private fun showData() {
        model.lista.observe(viewLifecycleOwner, Observer {
            cadapter.diff.submitList(it)

        })

    }

}