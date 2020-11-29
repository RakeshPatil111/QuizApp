package `in`.techashram.quizapp.ui.fragments

import `in`.techashram.quizapp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    var currentUserId : String = ""
    var quizId : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get user
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            currentUserId = firebaseAuth.currentUser!!.uid
        }
        else {
            // go to home fragmnt
        }

        quizId = ResultFragmentArgs.fromBundle(requireArguments()).quizId

        firebaseFirestore = FirebaseFirestore.getInstance()

        firebaseFirestore.collection("QuizList")
            .document(quizId)
            .collection("Results")
            .document(currentUserId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.getResult()
                    tvCorrect.text = result?.get("correct").toString()
                    tvWrong.text = result?.get("wrong").toString()
                    tvMissed.text = result?.get("missed").toString()
                    var percentage =  result?.get("correct").toString().toInt() * 100 / 10
                    tvPercent.text = ""+percentage+"%"
                    percent.progress = percentage
                }
            }
    }
}