package com.ss.delivery.booking.garage.presenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ss.delivery.booking.garage.R
import com.ss.delivery.booking.garage.data.model.TimeSlot

class SlotChildAdapter(
    private val context: Context,
    private val arrayList: ArrayList<TimeSlot>,
    private var onClick: OnCheckBoxClick,
    private val mPosition: Int
) :
    RecyclerView.Adapter<SlotChildAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtSlotName)
        val cbSLot: CheckBox = itemView.findViewById(R.id.cbSlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.slot_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = arrayList[position].name
        holder.cbSLot.isChecked = arrayList[position].status!!

        holder.cbSLot.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked)
                onClick.onCbClick(mPosition, position)
            else
                holder.cbSLot.isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}

interface OnCheckBoxClick {
    fun onCbClick(timePosition: Int, slotPosition: Int)
}