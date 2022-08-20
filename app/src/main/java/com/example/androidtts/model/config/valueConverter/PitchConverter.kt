package com.example.androidtts.model.config.valueConverter

/**
 * tts 객체의 말의 높낮이 값을 관리하는 매니저 (범위: 0.5 ~ 1.5)
 **/
class PitchConverter: ValueConverter() {
    //0 ~ 100의 사용자 입력값을 0.5 ~ 1.5으로 맞추기 위한 상수들( a * process + b = value 식에서 a,b)
    override val multipleConstant = (1F/100)
    override val addConstant = 0.5F

    //pitch 기본값
    override val initValue: Float = 1F
}