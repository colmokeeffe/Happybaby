package ie.wit.happybaby.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.happybaby.databinding.CardActivityGalleryBinding
import ie.wit.happybaby.models.ActivityGalleryModel


interface ImageListener {
    fun onActivityGalleryClick(gallery: ActivityGalleryModel)
}

class ActivityGalleryAdapter(
    private var galleries: MutableList<ActivityGalleryModel>,
    private val listener2: ImageListener) :
            RecyclerView.Adapter<ActivityGalleryAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardActivityGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val gallery = galleries[holder.adapterPosition]
        holder.bind(gallery, listener2)
    }

    override fun getItemCount(): Int = galleries.size

    fun removeAt(position: Int) {
        galleries.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder(private val binding : CardActivityGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gallery: ActivityGalleryModel, listener2: ImageListener) {
            binding.imageTitle.text = gallery.imagetitle
            binding.imageDescription.text = gallery.imagedescription
            if (gallery.image != "") {
                Picasso.get().load(gallery.image).resize(250, 250).into(binding.imageIcon)
            }
            binding.imageCategory.text = gallery.imagecategory
            if (gallery.favourite) {
                binding.imagefavourite.setVisibility(View.VISIBLE)
            } else {
                binding.imagefavourite.setVisibility(View.GONE)
            }
            binding.root.setOnClickListener { listener2.onActivityGalleryClick(gallery) }
        }
    }
}