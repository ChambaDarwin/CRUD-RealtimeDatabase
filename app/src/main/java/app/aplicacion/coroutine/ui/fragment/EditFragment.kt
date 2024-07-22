package app.aplicacion.coroutine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.aplicacion.coroutine.R
import app.aplicacion.coroutine.core.hideProgressBar
import app.aplicacion.coroutine.core.showProgressBar

import app.aplicacion.coroutine.ui.adapter.UserAdapter
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.databinding.FragmentEditBinding
import app.aplicacion.coroutine.ui.adapter.adapterviewpager.AdapterViewPager
import app.aplicacion.coroutine.ui.viewmodel.UserViewModel
import app.aplicacion.coroutine.util.DataState
import app.aplicacion.coroutine.util.ValidateField

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var model: UserViewModel
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var pager:AdapterViewPager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentEditBinding.inflate(inflater,container,false)
       model=ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        pager= AdapterViewPager()
        introducirDatos()

        binding.btnRegistrar.setOnClickListener {
            modficarUsuario()
        }


        observeUpdate()
        observeField()
        binding.viewPager2.adapter=pager
        return binding.root
    }

    private fun modficarUsuario(){
        val nombre=binding.nombre.text.toString()
        val apellido=binding.apellido.text.toString()
        val materia=binding.materia.text.toString()
        val email=binding.email.text.toString()

        val user= UserData(args.user.id,nombre, apellido, email, materia, null)
        model.updateUser(user)

    }
    private  fun observeUpdate(){
        model.updateData.observe(viewLifecycleOwner, Observer {
            when(it){
                is DataState.Error -> {
                    binding.progress.hideProgressBar()
                }
                is DataState.Loading -> {
                    binding.progress.showProgressBar()
                }
                is DataState.Sucess -> {
                    binding.progress.hideProgressBar()
                    Toast.makeText(requireContext(),"Registro modificado con exito",Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()

                }

                else -> {}
            }
        })
    }
    private fun observeField(){
        lifecycleScope.launchWhenStarted {
            model.validateUser.collect { user->
                if(user.email is ValidateField.Error){
                    binding.email.apply {
                        requestFocus()
                        setError(user.email.message)
                    }
                }
                if(user.nombre is ValidateField.Error){
                    binding.nombre.apply {
                        requestFocus()
                        setError(user.nombre.message)
                    }
                }
            }
        }

    }



    private fun introducirDatos(){
        pager.setListaImages(args.user.listaImages!!.donwload.toMutableList())

        binding.nombre.setText(args.user.nombre)
        binding.apellido.setText(args.user.apellido)
        binding.email.setText(args.user.email)
        binding.materia.setText(args.user.materia)


    }


}