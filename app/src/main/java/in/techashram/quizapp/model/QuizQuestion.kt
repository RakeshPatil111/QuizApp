package `in`.techashram.quizapp.model

import com.google.firebase.firestore.DocumentId

class QuizQuestion {
    @DocumentId
    var question_id : String = ""
    var question : String = ""
    var answer : String = ""
    var option_a : String = ""
    var option_b : String = ""
    var option_c : String = ""
}