package com.raywenderlich.financialapp

object Constants{

    const val TOTAL_QUESTIONS: String = "total_question"
    const val CORRECT_ANSWERS: String = "correct_answers"



    fun getQuestions(): ArrayList<Question?>{
        val questionList = ArrayList<Question?>()

        val que1 = Question(
            1,
            "Что нужно сделать с деньгами, которые остались у вас к следующей зарплате?",
            "Сохранить",
            "Потратить",
            "Положить в банк",
            "Положить в банку",
            1
        )

        val que2 = Question(
            2,
            "Что нужно сделать с деньгами, которые остались у вас к следующей зарплате?",
            "Сохранить",
            "Потратить",
            "Положить в банк",
            "Положить в банку",
            1
        )


        questionList.add(que1)
        questionList.add(que2)

        return questionList
    }

}