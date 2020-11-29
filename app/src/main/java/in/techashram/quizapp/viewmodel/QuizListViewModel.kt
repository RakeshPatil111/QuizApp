package `in`.techashram.quizapp.viewmodel

import `in`.techashram.quizapp.model.QuizList
import `in`.techashram.quizapp.repository.FirebaseRepository
import `in`.techashram.quizapp.repository.FirebaseRepository.OnFirestoreTaskCompleted
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

class QuizListViewModel : ViewModel(),OnFirestoreTaskCompleted {

    val firebaseResult = FirebaseRepository(this)

    init {
        firebaseResult.getQuizData()
    }
    var quizListLiveData = MutableLiveData<List<QuizList>>()
    override fun quizListAdded(list: List<QuizList>) {
        quizListLiveData.value = list
    }

    override fun onError(e: Exception) {
        TODO("Not yet implemented")
    }
}