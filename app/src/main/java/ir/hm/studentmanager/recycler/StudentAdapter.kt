package ir.hm.studentmanager.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.hm.studentmanager.databinding.RecyclerItemBinding

class StudentAdapter(
    private val data: ArrayList<Student>,
    private val studendEvents: StudendEvents
) : RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {

    lateinit var binding: RecyclerItemBinding

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(student: Student) {

            binding.txtName.text = student.name.toString()
            binding.txtCourse.text = student.course
            binding.txtHarfAval.text = student.name[0].toString().uppercase()
            binding.txtScore.text = student.score.toString()



            itemView.setOnClickListener {
                studendEvents.onItemClicked(student, adapterPosition)
            }

            itemView.setOnLongClickListener {
                studendEvents.onItemLongClicked(student, adapterPosition)
                true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindViews(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun removeItem(student: Student, position: Int) {
        data.remove(student)
        notifyItemRemoved(position)
    }

    fun addItem(student: Student) {
        data.add(0, student)
        notifyItemInserted(0)
    }

    fun updateItem(student: Student, position: Int) {
        data.set(position, student)
        notifyItemChanged(position)
    }
}

interface StudendEvents {
    fun onItemClicked(student: Student, position: Int)
    fun onItemLongClicked(student: Student, position: Int)

}