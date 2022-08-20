package com.example.androidtts.model.config

import com.example.androidtts.model.config.valueConverter.PitchConverter
import com.example.androidtts.model.config.valueConverter.RateConverter
import com.example.androidtts.model.config.valueConverter.ValueConverter
import java.util.*

/**
 * tts 객체 속성 관련 데이터를 driver 모델에게 제공할 config 모델.
 * 변경할 수 있는 3가지의 tts 속성인 높낮이, 빠르기, 언어를 각 담당 객체를 통해 제공한다.
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

    /* pitch 시작 값을 반환하는 함수 */
    fun getInitPitch(): Float{
        return pitchConverter.initValue
    }

    /* rate 시작 값을 반환하는 함수 */
    fun getInitRate(): Float{
        return rateConverter.initValue
    }

    /* 언어 시작 값을 반환하는 함수 */
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