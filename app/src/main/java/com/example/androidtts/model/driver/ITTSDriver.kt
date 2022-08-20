package com.example.androidtts.model.driver

import com.example.androidtts.model.config.TTSConfig
import java.io.File

/**
 * Driver 모델이 해야하는 역할을 규정하는 인터페이스.
 * 각 환경에 따라 다른 구체화된 객체들이 이 인터페이스를 구현해서 사용한다.
 */
interface ITTSDriver {
    //Driver 시작 세팅, 종료 세팅

    /* TTS 객체를 위한 config 모델을 세팅하는 함수 */
    fun setConfiguration(config: TTSConfig)

    /* 모델에 설정된 pitch 시작값대로 tts 속성 및 뷰를 세팅하는 함수 */
    fun initPitch(): Float

    /* 모델에 설정된 rate 시작값대로 tts 속성 및 뷰를 세팅하는 함수 */
    fun initRate(): Float

    /* 모델에 설정된 language 시작값대로 tts 속성 및 뷰를 세팅하는 함수 */
    fun initLanguage(): String

    /* TTS 객체를 완전히 종료하는 함수 */
    fun destroy()


    //Driver 설정 값 변경 관련

    /* TTS 객체의 음성 높낮이를 지정하는 함수 */
    fun changePitch(progress: Int): Float

    /* TTS 객체의 음성 속도를 지정하는 함수 */
    fun changeRate(progress: Int): Float

    /* TTS 객체의 언어를 지정하는 함수 */
    fun changeLanguage(lang: String)


    //Driver 주체적 행위 관련

    /* TTS 객체에서 실제 음성을 얻는 함수 */
    fun speak(contents: String)

    /* TTS 객체를 통해 그 음성을 파일로 다운로드받는 함수 */
    fun insertSpeechInFile(contents: String, file: File): Int
}