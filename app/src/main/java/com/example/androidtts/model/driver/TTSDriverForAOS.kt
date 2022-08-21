package com.example.androidtts.model.driver

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.androidtts.model.config.TTSConfig
import java.io.File

/**
 * 안드로이드가 제공하는 textToSpeech 기능으로 driver 기능을 수행하는 모델 객체.
 * 어떤 앱 콘텍스트에서 tts 객체가 동작할지 알기 위해 인자로 앱 콘텍스트를 받아 적용한다.
 **/
class TTSDriverForAOS(context: Context): ITTSDriver {

    //--------------------------------------------------
    //인스턴스 영역
    //

    //실제 tts 인스턴스
    private val textToSpeech: TextToSpeech = TextToSpeech(context){}

    //Driver 객체와 연결될 config
    private lateinit var config: TTSConfig


    //--------------------------------------------------
    //함수 영역
    //

    //Driver 시작 세팅, 종료 세팅

    /* TTS 객체를 위한 config 모델을 세팅하는 함수 */
    override fun setConfiguration(config: TTSConfig) {
        this.config = config
    }

    /* 모델에 설정된 pitch 시작값대로 tts 속성 및 뷰를 세팅하는 함수 */
    override fun initPitch(): Float{
        val defaultPitch = config.getInitPitch()
        textToSpeech.setPitch(defaultPitch)
        return defaultPitch
    }

    /* 모델에 설정된 rate 시작값대로 tts 속성 및 뷰를 세팅하는 함수 */
    override fun initRate(): Float{
        val defaultRate = config.getInitRate()
        textToSpeech.setSpeechRate(defaultRate)
        return defaultRate
    }

    /* 모델에 설정된 language 시작값대로 tts 속성 및 뷰를 세팅하는 함수 */
    override fun initLanguage(): String {
        val defaultLanguage = config.getInitLanguage()
        textToSpeech.language = config.languageChange(defaultLanguage)
        return defaultLanguage
    }

    /* TTS 객체를 완전히 종료하는 함수 */
    override fun destroy(){
        textToSpeech.stop()
        textToSpeech.shutdown()
    }


    //Driver 설정 값 변경 관련

    /* TTS 객체의 음성 높낮이를 지정하는 함수 */
    override fun changePitch(progress: Int): Float{
        val pitch = config.pitchChange(progress)
        return pitch.also { textToSpeech.setPitch(it) }
    }

    /* TTS 객체의 음성 속도를 지정하는 함수 */
    override fun changeRate(progress: Int): Float{
        val rate = config.rateChange(progress)
        return rate.also { textToSpeech.setSpeechRate(it) }
    }

    /* TTS 객체의 언어를 지정하는 함수 */
    override fun changeLanguage(lang: String){
        textToSpeech.language = config.languageChange(lang)
    }


    //Driver 주체적 행위 관련

    /* TTS 객체에서 실제 음성을 얻는 함수 */
    override fun speak(contents: String){
        textToSpeech.speak(contents, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    /* TTS 객체를 통해 그 음성을 파일로 다운로드받는 함수
        파일에 음성 데이터 합성 (빈 파일에 음성 내용 넣기) -> 성공 여부 반환 */
    override fun insertSpeechInFile(contents: String, file : File): Int{
        return textToSpeech.synthesizeToFile(contents, null, file, null)
    }
}