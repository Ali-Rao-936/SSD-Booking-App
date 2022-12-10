package com.ss.delivery.booking.garage.presenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.TimeModel

class HomeTimeAdapter(
    private val context: Context, private var arrayList: ArrayList<TimeModel>,
    private var onClick: OnCheckBoxClick
) :
    RecyclerView.Adapter<HomeTimeAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val rvSlots: RecyclerView = itemView.findViewById(R.id.rvSlots)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.home_item_view_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTime.text = arrayList[position].value

        val childLayoutManager = GridLayoutManager(context, 3)

        holder.itemView.tag = position

        holder.rvSlots.apply {
            layoutManager = childLayoutManager
            adapter =
                arrayList[position].slots?.let { SlotChildAdapter(context, it, onClick, holder.itemView) }
            setRecycledViewPool(viewPool)
        }
    }

    fun updateAdapter(arrayList: ArrayList<TimeModel>){
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}