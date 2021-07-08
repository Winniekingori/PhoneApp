import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.ContactModel
import com.example.phoneapp.R

class ContactAdapter(val contacts: List<ContactModel>, val listener: (ContactModel) -> Unit) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView=LayoutInflater.from(parent.context).inflate(R.layout.contact_rv_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position], listener)
    }


    override fun getItemCount() = contacts.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contactModel: ContactModel, listener: (ContactModel) -> Unit) {


        }
    }
        }


