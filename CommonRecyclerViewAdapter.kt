import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class CommonRecyclerViewAdapter<T, B : ViewBinding> (
    val display: (B,T) -> Unit,
    val inflate: (LayoutInflater, ViewGroup) -> B
): ListAdapter<T, CommonRecyclerViewViewHolder<T, B>>(BaseDiffUtil<T>()) {

    private val viewBindingRepresentation = object: ViewBindingRepresentation<T,B> {
        override fun display(binding: B, item: T) {
            this@CommonRecyclerViewAdapter.display(binding, item)
        }
    }
    private val viewBindingInflater = object: ViewBindingInflater<B>{
        override fun inflateBinding(layoutInflater: LayoutInflater, parent: ViewGroup): B {
            return inflate(layoutInflater, parent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  CommonRecyclerViewViewHolder<T,B> {
        return CommonRecyclerViewViewHolder.from(parent, viewBindingRepresentation, viewBindingInflater)
    }

    override fun onBindViewHolder(holder: CommonRecyclerViewViewHolder<T, B>, position: Int) {
        val item = getItem(position) as T
        holder.bind(item)
    }
}


class CommonRecyclerViewViewHolder<T, B : ViewBinding>(
    private val binding: B,
    private val viewBindingRepresentation: ViewBindingRepresentation<T, B>
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun <T, B : ViewBinding> from(
            parent: ViewGroup,
            viewBindingRepresentation: ViewBindingRepresentation<T, B>,
            viewBindingInflater: ViewBindingInflater<B>
        ): CommonRecyclerViewViewHolder<T, B> {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemView = viewBindingInflater.inflateBinding(layoutInflater, parent)
            return CommonRecyclerViewViewHolder(itemView, viewBindingRepresentation)
        }
    }

    fun bind(item: T) {
        viewBindingRepresentation.display(binding, item)
    }
}

interface ViewBindingRepresentation<T, B : ViewBinding> {
    fun display(binding: B, item: T)
}

interface ViewBindingInflater<B : ViewBinding> {
    fun inflateBinding(layoutInflater: LayoutInflater, parent: ViewGroup): B
}
