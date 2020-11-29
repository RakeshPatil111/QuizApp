package `in`.techashram.quizapp.repository

import `in`.techashram.quizapp.model.QuizList
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class FirebaseRepository(var onFirestoreTaskCompleted: OnFirestoreTaskCompleted) {
    val firestore = FirebaseFirestore.getInstance()
    val quizReference  = firestore.collection("QuizList")

    fun getQuizData()
    {
        quizReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                onFirestoreTaskCompleted.quizListAdded(it.result!!.toObjects(QuizList::class.java))
            } else {
                onFirestoreTaskCompleted.onError(it.exception!!)
            }
        }
    }

    public interface OnFirestoreTaskCompleted {
        fun quizListAdded(list : List<QuizList>)
        fun onError(e : Exception)
    }
}