package gtpay.gtronicspay.c.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(){

    private val _liveScore = MutableLiveData<Int>(0)
    val liveScore : LiveData<Int> = _liveScore

    fun increaseScore(){
        val currentScores = _liveScore.value
        val increasedScores = currentScores!!.plus(randomInt())
        _liveScore.postValue(increasedScores)
    }

    private fun randomInt() : Int{
        return (1..20).random()
    }
}