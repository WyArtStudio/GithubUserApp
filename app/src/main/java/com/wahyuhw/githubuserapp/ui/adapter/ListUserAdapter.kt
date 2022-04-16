package com.wahyuhw.githubuserapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyuhw.githubuserapp.databinding.ItemListUserBinding
import com.wahyuhw.githubuserapp.data.shared.User
import com.wahyuhw.githubuserapp.ui.activity.DetailActivity

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private val listUser = ArrayList<User>()

    fun setUsersData(dataResponse: List<User>) {
        listUser.clear()
        listUser.addAll(dataResponse)
        notifyDataSetChanged()
    }

    fun clearData() {
        listUser.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvName.text = user.username
                Glide.with(itemView.context).load(user.avatarUrl).into(imgProfile)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USER, user)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}