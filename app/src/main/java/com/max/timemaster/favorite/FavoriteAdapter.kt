package com.max.timemaster.favorite

import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.max.timemaster.R
import com.max.timemaster.TimeMasterApplication
import com.max.timemaster.data.DateFavorite
import com.max.timemaster.databinding.ItemFavoriteBinding
import com.max.timemaster.util.SetColorStateList
import com.max.timemaster.util.UserManager

class FavoriteAdapter :
    ListAdapter<DateFavorite, FavoriteAdapter.FavoriteItemViewHolder>(
        DiffCallback
    ) {

    class FavoriteItemViewHolder(private var binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(dateFavorite: DateFavorite) {

            val dateColor = UserManager.myDate.value?.filter {
                it.name == dateFavorite.attendeeName
            }?.get(0)?.color ?: ""
            val colorsStateList = dateColor.let { color ->
                SetColorStateList.setColorStateList(color)
            }
            val chipGroup = binding.chipGroup
            val contentList = dateFavorite.favoriteContent

            binding.title.text = dateFavorite.favoriteTitle
            binding.attendee.text = dateFavorite.attendeeName
            binding.view.backgroundTintList = colorsStateList


            chipGroup.removeAllViews()

            if (contentList != null) {
                for (index in contentList.indices) {

                    val content = contentList[index]
                    val chip = Chip(chipGroup.context)
                    val paddingDp = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10f,
                        TimeMasterApplication.instance.resources.displayMetrics
                    ).toInt()

                    chip.text = content
                    chip.textSize = 12f
                    chip.setTextColor(Color.BLACK)
                    chip.chipBackgroundColor = colorsStateList
                    chip.closeIconTint = SetColorStateList.setColorStateList(
                        TimeMasterApplication.instance.getString(R.string.color_white_text)
                    )
                    chip.setPadding(40, paddingDp, paddingDp, paddingDp)
                    chipGroup.setBackgroundColor(Color.parseColor("#FCFCFC"))
                    chipGroup.addView(chip)

                    binding.executePendingBindings()
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DateFavorite>() {
        override fun areItemsTheSame(
            oldItem: DateFavorite,
            newItem: DateFavorite
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DateFavorite,
            newItem: DateFavorite
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteItemViewHolder {
        return FavoriteItemViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        val favorite =
            getItem(position)
        holder.bind(favorite)
    }

}