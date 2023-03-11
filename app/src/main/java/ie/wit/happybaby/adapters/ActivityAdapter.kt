package ie.wit.happybaby.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.happybaby.databinding.CardActivityBinding
import ie.wit.happybaby.models.ActivityModel

interface ActivityListener {
    fun onActivityClick(activity: ActivityModel)
}

class ActivityAdapter constructor(private var activities: MutableList<ActivityModel>,
                                    private val listener: ActivityListener) :
            RecyclerView.Adapter<ActivityAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardActivityBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val activity = activities[holder.adapterPosition]
        holder.bind(activity, listener)
    }

    override fun getItemCount(): Int = activities.size

    fun removeAt(position: Int) {
        activities.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder(private val binding : CardActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: ActivityModel, listener: ActivityListener) {
            binding.dateTextView.text = activity.selectedDate
            binding.timeTextView.text = activity.selectedTime
            binding.activityDescription.text = activity.description
            binding.activityCategory.text = activity.category
            binding.cardActivityRating.rating = activity.rating
            if (activity.priority) {
                binding.imagepriority.setVisibility(View.VISIBLE)
            } else {
                binding.imagepriority.setVisibility(View.GONE)
            }
            binding.root.setOnClickListener { listener.onActivityClick(activity) }
        }
    }
}