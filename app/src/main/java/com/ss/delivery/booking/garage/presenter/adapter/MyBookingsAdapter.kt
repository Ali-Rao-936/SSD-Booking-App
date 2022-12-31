package com.ss.delivery.booking.garage.presenter.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ss.delivery.booking.garage.data.model.MyBooking
import com.ss.delivery.booking.garage.databinding.MyBookingItemLayoutBinding
import com.ss.delivery.booking.garage.presenter.settings.BookingDetailsActivity

class MyBookingsAdapter(private val list: ArrayList<MyBooking>, val context: Context) :
    RecyclerView.Adapter<MyBookingsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MyBookingItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.root.setOnClickListener { context.startActivity(Intent(context, BookingDetailsActivity::class.java).putExtra("detailsPosition", adapterPosition)) }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MyBookingItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtBookingId.text = "Booking Id: ${list[position].ID}"
        holder.binding.txtTime.text = list[position].Time
        holder.binding.txtHeading.text = list[position].Type
    }

    override fun getItemCount(): Int {
        return list.size
    }
}