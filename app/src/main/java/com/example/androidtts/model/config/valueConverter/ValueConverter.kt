package com.example.androidtts.model.config.valueConverter

/**
 * tts 객체의 속성 값을 관리하는 매니저 (추상 클래스)
 * float 자료형 값을 통해 지정할 수 있고 범위가 존재하는 속성들을 관리한다.
 * 기본적으로 0 ~ 100으로 들어오는 사용자 입력을 속성 범위에 맞게 변환(mapping)하는 역할을 한다.
 **/
abstract class ValueConverter {
    //0~100으로 들어온 값을 범위에 맞게 변환하기 위한 상수
    protected abstract val multipleConstant: Float
    protected abstract val addConstant: Float

    //각 속성들의 기본값
    abstract val initValue: Float

    /* 0 ~ 100 사이 값을 받아 속성 값을 만들고 반환하는 함수 */
    fun convertProgressToValue(progress: Int): Float{
        return (multipleConstant * progress + addConstant)
    }
}