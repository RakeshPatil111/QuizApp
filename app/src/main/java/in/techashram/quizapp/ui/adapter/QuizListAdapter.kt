package `in`.techashram.quizapp.ui.adapter

import `in`.techashram.quizapp.R
import `in`.techashram.quizapp.model.QuizList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_quiz.view.*

class QuizListAdapter : RecyclerView.Adapter<QuizListAdapter.QuizListHolder>() {
    private var clickedAction: ((QuizList) -> Unit)? = null
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<QuizList>() {
            override fun areItemsTheSame(oldItem: QuizList, newItem: QuizList): Boolean {
                return oldItem.quiz_id.equals(newItem.quiz_id)
            }

            override fun areContentsTheSame(oldItem: QuizList, newItem: QuizList): Boolean {
                return oldItem.quiz_id.equals(newItem.quiz_id)
            }

        }
    }
    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = QuizListHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: QuizListHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun onItemClicked(action : (QuizList) -> Unit) {
        this.clickedAction = action
    }
    inner class QuizListHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(quizList: QuizList?) {
            view.tvTitle.text = quizList?.name
            view.tvDesrciption.text = quizList?.description
            view.tvLevel.text = quizList?.level
            clickedAction?.let {
                view.btnStart.setOnClickListener {
                    it(quizList!!)
                }
            }
            Glide
                .with(view.ivQuiz.context)
                .load(quizList?.image)
                .centerCrop()
                .into(view.ivQuiz)
        }
    }
}