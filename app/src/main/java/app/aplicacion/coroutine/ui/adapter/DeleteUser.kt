package app.aplicacion.coroutine.ui.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class DeleteUser :ItemTouchHelper.Callback(){
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val position=ItemTouchHelper.LEFT or ItemTouchHelper.DOWN or
                ItemTouchHelper.UP or ItemTouchHelper.RIGHT
        return makeMovementFlags(0,position)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}