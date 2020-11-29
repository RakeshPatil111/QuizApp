package `in`.techashram.quizapp.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

class QuizList(
) : Serializable {
    var description : String = ""
    var image : String = ""
    var level : String = ""
    var name : String = ""
    var questions : Long = 0L
    @DocumentId
    var quiz_id : String = ""
}