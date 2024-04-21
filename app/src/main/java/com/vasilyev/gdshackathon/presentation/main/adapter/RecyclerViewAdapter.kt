package com.vasilyev.gdshackathon.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vasilyev.gdshackathon.R
import com.vasilyev.gdshackathon.domain.entity.Message

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    private var list = mutableListOf<Message>()

    fun addMessage(message: Message){
        list.add(message)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(view: View) : ViewHolder(view){
        val message: TextView = view.findViewById(R.id.msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if(viewType == 1){
            MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ai_msg, parent, false))
        }else{
            MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_msg, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].author == "ai") 1
            else 2
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.message.text = item.content
    }
}