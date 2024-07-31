package app.aplicacion.coroutine.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.aplicacion.coroutine.R
import app.aplicacion.coroutine.core.hideProgressBar
import app.aplicacion.coroutine.core.showProgressBar
import app.aplicacion.coroutine.core.toast
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.databinding.FragmentEdiBinding

import app.aplicacion.coroutine.ui.adapter.adapterviewpager.AdapterViewPager
import app.aplicacion.coroutine.ui.viewmodel.UserViewModel
import app.aplicacion.coroutine.util.DataState
import app.aplicacion.coroutine.util.ValidateField
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class EdiFragment : Fragment() {
    private lateinit var binding: FragmentEdiBinding
    private lateinit var model: UserViewModel
    private val args by navArgs<EdiFragmentArgs>()
    private lateinit var pager: AdapterViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEdiBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        pager = AdapterViewPager()
        introducirDatos()

        binding.btnRegistrar.setOnClickListener {
            modificarUsuario()
        }
        observeEmpty()
        observeUpdate()
        binding.viewPager2.adapter = pager


        return binding.root
    }


    private fun modificarUsuario() {
        val nombre = binding.nombre.text.toString()
        val apellido = binding.apellido.text.toString()
        val materia = binding.materia.text.toString()
        val email = binding.email.text.toString()

        val user = UserData(args.user.id, nombre, apellido, email, materia, args.user.image)

        model.updateUserReal(user)

    }

    private fun observeUpdate() {
        lifecycleScope.launchWhenStarted {
            model.userDataState.collect { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                        binding.progress.showProgressBar()
                    }

                    is DataState.Sucess -> {
                        binding.progress.hideProgressBar()
                        findNavController().popBackStack()
                        toast("registro modificado con exito")
                        model._userDataState.value = null

                    }

                    is DataState.Error -> {
                        binding.progress.hideProgressBar()
                        toast(dataState.message.toString())
                    }

                    else -> {}
                }
            }
        }


    }

    private fun introducirDatos() {
        pager.setListaImages(args.user.image!!.donwload.toMutableList())
        binding.nombre.setText(args.user.nombre)
        binding.apellido.setText(args.user.apellido)
        binding.email.setText(args.user.email)
        binding.materia.setText(args.user.materia)
    }

    private fun observeEmpty() {
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


    private fun vaciarCampos() {
        binding.nombre.setText("")
        binding.apellido.setText("")
        binding.email.setText("")
        binding.materia.setText("")
    }
}

