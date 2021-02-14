package com.example.maps.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.maps.R
import com.example.maps.Task
import com.example.maps.View.TaskMenuActivity
import kotlinx.android.synthetic.main.task_recyclerview.view.*


class TaskAdapter(private val taskList: List<Task>, val context: Context) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun getItemCount() = taskList.size

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.task_recyclerview,p0,false)
        return TaskViewHolder(
            itemView,
            context
        )
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = taskList[position]
        holder.textTitle?.setText(currentItem.title)
        holder.textExp?.setText(currentItem.description)
        holder.textLocationLg?.setText(currentItem.longitude.toString())
        holder.textLocationLt?.setText(currentItem.latitude.toString())
        var task = (holder.myMapsActivity as TaskMenuActivity)
        val intent = Intent(context, task::class.java)
        holder.butonDetay.visibility = View.GONE
        holder.butonKonum.visibility = View.GONE

        holder.butonDetay.setOnClickListener {
            intent.putExtra("info","task")
            intent.putExtra("id",currentItem.Id)
            context.startActivity(intent)
        }
        holder.butonKonum.setOnClickListener {
            intent.putExtra("info","konum")
            intent.putExtra("id",currentItem.Id)
            context.startActivity(intent)
        }
        holder.item.setOnClickListener{
            holder.butonDetay.visibility = View.VISIBLE
            holder.butonKonum.visibility = View.VISIBLE
        }

    }
    class TaskViewHolder(itemView: View, myMapsActivity: Context) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.rec_id
        val textTitle = itemView.title_tv
        val textExp = itemView.exp_tv
        val textLocationLg = itemView.location
        val textLocationLt = itemView.location_tv
        val butonDetay = itemView.btnDetay
        val butonKonum = itemView.btnKonum
        val myMapsActivity:Context=myMapsActivity
        val item = itemView
    }
}