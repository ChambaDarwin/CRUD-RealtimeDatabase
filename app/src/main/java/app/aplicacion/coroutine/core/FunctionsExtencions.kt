package app.aplicacion.coroutine.core

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message:String){
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
}
fun View.hideProgressBar(){
    visibility=View.INVISIBLE
}

fun View.showProgressBar(){
    visibility=View.VISIBLE
}