package `in`.techashram.quizapp.ui.fragments

import `in`.techashram.quizapp.R
import `in`.techashram.quizapp.model.QuizList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.item_quiz.view.*
import kotlinx.android.synthetic.main.item_quiz.view.ivQuiz

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quiz = DetailsFragmentArgs.fromBundle(requireArguments()).quiz
        setData(quiz)
        btnStartQuiz.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToQueFragment(quiz.quiz_id, quiz.name)
            findNavController().navigate(action)
        }
    }

    private fun setData(quiz: QuizList) {
        Glide
            .with(ivQuiz.context)
            .load(quiz?.image)
            .centerCrop()
            .into(ivQuiz)
        tvDesrciption.text = quiz.description
        tvTitle.text = quiz.name
        difficulty.text = quiz.level
        totalQuestions.text = quiz.questions.toString()
    }
}