package gtpay.gtronicspay.c.usecases

class TimeUseCase {
    fun getData() : Float{
        return System.currentTimeMillis() / 1000f
    }
}