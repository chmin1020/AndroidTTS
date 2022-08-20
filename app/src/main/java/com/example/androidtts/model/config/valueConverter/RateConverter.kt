package com.example.androidtts.model.config.valueConverter

/**
 * tts 객체의 말 속도 속성 값을 관리하는 매니저 (범위: 0.1 ~ 4.0)
 **/
class RateConverter: ValueConverter() {
    //0 ~ 100의 사용자 입력값을 0.1 ~ 4.0으로 맞추기 위한 상수들( a * process + b = value 식에서 a,b)
    override val multipleConstant = (3.9F/100)
    override val addConstant = 0.1F

    //speechRate 기본값
    override val initValue: Float = 1F
}