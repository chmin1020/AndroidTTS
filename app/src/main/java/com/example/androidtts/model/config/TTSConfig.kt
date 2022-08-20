package com.example.androidtts.model.config

import com.example.androidtts.model.config.valueConverter.PitchConverter
import com.example.androidtts.model.config.valueConverter.RateConverter
import com.example.androidtts.model.config.valueConverter.ValueConverter
import java.util.*

/**
 * tts 객체의 데이터 저장 및 갱신을 담당하는 모델 객체
 * 선택자와 매니저 객체의 인스턴스를 가지고 필요한 속성 값 변경을 진행한다.
 **/
class TTSConfig {
    //--------------------------------------------------
    //인스턴스 영역
    //

    //언어 선택자, 변환기(높낮이, 속도)
    private val languageSelector: LanguageSelector = LanguageSelector()
    private val pitchConverter: ValueConverter = PitchConverter()
    private val rateConverter: ValueConverter = RateConverter()

    //--------------------------------------------------
    //함수 영역
    //

    /* pitch 기본 값을 반환하는 함수 */
    fun getInitPitch(): Float{
        return pitchConverter.initValue
    }

    /* rate 기본 값을 반환하는 함수 */
    fun getInitRate(): Float{
        return rateConverter.initValue
    }

    /* 언어 기본 값을 반환하는 함수 */
    fun getInitLanguage(): String{
        return languageSelector.initLanguage
    }

    /* 목소리 pitch 변경하는 함수 */
    fun pitchChange(progress: Int): Float{
        return pitchConverter.convertProgressToValue(progress)
    }

    /*목소리 rate 변경하는 함수 */
    fun rateChange(progress: Int): Float{
        return rateConverter.convertProgressToValue(progress)
    }

    /* 언어 설정하는 함수 */
    fun languageChange(str: String): Locale{
        return languageSelector.selectNewLanguage(str)
    }
}