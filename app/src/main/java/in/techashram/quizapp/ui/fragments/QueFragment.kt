package `in`.techashram.quizapp.ui.fragments

import `in`.techashram.quizapp.R
import `in`.techashram.quizapp.model.QuizQuestion
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_que.*
import java.util.*
import kotlin.collections.HashMap

class QueFragment : Fragment(), View.OnClickListener {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var allQuestions : MutableList<QuizQuestion>
    val numberOfQueToAns = 10
    private var questionToAnswer : MutableList<QuizQuestion> = mutableListOf()
    lateinit var counterTimer: CountDownTimer
    var canAnswer = false
    var currentQuestion = 0
    var correctAnswers : Int = 0
    var wrongAnswers : Int = 0
    var missed : Int = 0
    lateinit var quiz_id : String
    lateinit var currentUserId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_que, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get selected quiz
        quiz_id = QueFragmentArgs.fromBundle(requireArguments()).quizId

        // get user
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            currentUserId = firebaseAuth.currentUser!!.uid
        }
        else {
            // go to home fragmnt
        }
        // get all questions from firestore
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("QuizList")
            .document(quiz_id).collection("QuizQuestions")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    allQuestions = it.getResult()?.toObjects(QuizQuestion::class.java) as MutableList<QuizQuestion>

                    // get 10 question
                    pickQuestions()

                    // load ui
                    loadUI()
                }
                else {
                    loadingQuiz.text = "Error can not load"
                }
            }

        // add button clicks
        btnOption1.setOnClickListener(this)
        btnOption2.setOnClickListener(this)
        btnOption3.setOnClickListener(this)
        btnNextQue.setOnClickListener(this)
    }

    private fun loadUI() {
        loadingQuiz.text = QueFragmentArgs.fromBundle(requireArguments()).quizName

        // enable buttos
        enableOptions()

        // load questions
        loadQuestion(0)
    }

    private fun startTimer() {
        progressBar.visibility = View.VISIBLE
        counterTimer = object : CountDownTimer(1000 * 15, 1000) {
            override fun onFinish() {
                missed++
                tvVerifying.text = "Time up!!!! Go to Next Question"
                showNextButton()
                canAnswer = false
            }

            override fun onTick(millisUntilFinished: Long) {
                // set count down time
                tvCount.text = ""+millisUntilFinished/1000
            }
        }

        counterTimer.start()
    }

    private fun loadQuestion(questionNumber: Int) {
        // set question numbr
        number.text = ""+(questionNumber+1)

        // set question
        tvQuestion.text = questionToAnswer.get(questionNumber).question

        // set options
        btnOption1.text = questionToAnswer.get(questionNumber).option_a
        btnOption2.text = questionToAnswer.get(questionNumber).option_b
        btnOption3.text = questionToAnswer.get(questionNumber).option_c

        // set user can answer
        canAnswer = true

        // current question number
        currentQuestion = questionNumber
        // count down timer
        startTimer()
    }

    private fun enableOptions() {
        btnOption1.visibility = View.VISIBLE
        btnOption2.visibility = View.VISIBLE
        btnOption3.visibility = View.VISIBLE

        // set enabled
        btnOption1.isEnabled = true
        btnOption2.isEnabled = true
        btnOption3.isEnabled = true

        // btn next
        btnNextQue.visibility = View.INVISIBLE
        btnNextQue.isEnabled = false
    }

    private fun pickQuestions() {
        for (i in 0..numberOfQueToAns-1) {
            var randomNumber = getRandomInteger()
            questionToAnswer.add(allQuestions.get(randomNumber))
        }
    }

    // generate random number
    fun getRandomInteger() = (0..allQuestions.size-1).random()

    // ans selected
    override fun onClick(v: View?) {
        when {
            v?.id == R.id.btnOption1 -> {
                answerSelected(btnOption1)
            }

            v?.id == R.id.btnOption2 -> {
                answerSelected(btnOption2)
            }

            v?.id == R.id.btnOption3 -> {
                answerSelected(btnOption3)
            }

            v?.id == R.id.btnNextQue -> {
                if (currentQuestion == questionToAnswer.size-1) {
                    submitResults()
                } else {
                    currentQuestion++
                    loadQuestion(currentQuestion)
                    resetUI()
                }

            }
        }
    }

    private fun submitResults() {
        // create hashmap
        var resultMap = HashMap<String, Any>()
        resultMap.put("correct", correctAnswers)
        resultMap.put("wrong", wrongAnswers)
        resultMap.put("missed", missed)
        firebaseFirestore.collection("QuizList")
            .document(quiz_id)
            .collection("Results")
            .document(currentUserId)
            .set(resultMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // go to result
                    val action = QueFragmentDirections.actionQueFragmentToResultFragment(quiz_id)
                    findNavController().navigate(action)
                }
                else {
                    // show error
                    loadingQuiz.setText(it.exception?.message)
                }
            }
    }

    private fun resetUI() {
        btnOption1.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        btnOption2.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        btnOption3.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))

        btnOption1.setTextColor(resources.getColor(R.color.textColorSecondary))
        btnOption2.setTextColor(resources.getColor(R.color.textColorSecondary))
        btnOption3.setTextColor(resources.getColor(R.color.textColorSecondary))

        tvVerifying.visibility = View.INVISIBLE
        btnNextQue.visibility = View.INVISIBLE
        btnNextQue.isEnabled = false
    }

    private fun answerSelected(selectedAns: MaterialButton) {

        if (canAnswer) {
            selectedAns.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            if (questionToAnswer.get(currentQuestion).answer.equals(selectedAns.text)) {
                tvVerifying.visibility = View.VISIBLE
                tvVerifying.text = "Correct Answer"
                selectedAns.setBackgroundColor(resources.getColor(R.color.colorGreen))
                correctAnswers++
            } else{
                tvVerifying.visibility = View.VISIBLE
                tvVerifying.text = "Wrong Answer"
                selectedAns.setBackgroundColor(resources.getColor(R.color.colorRed))
                wrongAnswers++
            }
        }
        canAnswer = false
        counterTimer.cancel()
        showNextButton()
    }

    private fun showNextButton() {
        if (currentQuestion == questionToAnswer.size-1) {
            btnNextQue.text = "Submit Results"
        }
        btnNextQue.isEnabled = true
        btnNextQue.visibility = View.VISIBLE
    }
}
