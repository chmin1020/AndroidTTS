package com.example.androidtts.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import com.example.androidtts.R
import com.example.androidtts.databinding.ActivityMainBinding
import com.example.androidtts.model.config.TTSConfig
import com.example.androidtts.model.driver.ITTSDriver
import com.example.androidtts.model.driver.TTSDriverForAOS
import com.example.androidtts.model.fileManager.ITTSFileManager
import com.example.androidtts.model.fileManager.TTSFileManagerForAOS

/**
 * 이 앱의 메인 화면 controller 역할을 하는 액티비티.
 * 이 화면에서는 tts 속성을 변경할 수 있고, 이를 통해 만들어진 음성을 듣거나 파일로 다운로드 받을 수 있다.
 * 따라서 모델은 기본적으로 driver 객체를 활용하되, 음성 파일 저장은 file manager 객체도 활용한다.
 * 뷰를 통해 사용자가 데이터 변경을 요청하면 모델에서 이것이 수행되고, 필요한 내용은 다시 뷰에 전달된다.
 **/
class MainActivity : AppCompatActivity() {
    //-------------------------------------------
    // 상수 영역
    //

    //언어 선택 스피너에 적용할 array 불러와서 사용
    val languages: Array<String> by lazy{ resources.getStringArray(R.array.language) }


    //-------------------------------------------
    // 인스턴스 영역
    //

    //tts 사용을 위한 모델 (실제 사용될 driver, 파일 저장을 위한 file manager<Singleton>)
    //안드로이드 환경이므로 안드로이드 OS 전용 모델을 활용
    private val ttsDriver: ITTSDriver by lazy{ TTSDriverForAOS(applicationContext) }
    private val ttsFileManager: ITTSFileManager by lazy{ TTSFileManagerForAOS(applicationContext) }

    //뷰 바인딩 객체
    private val binder: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    //-------------------------------------------
    // 생명 주기
    //

    /* onCreate()에서 뷰와 모델 관련 설정 사항 적용 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰와 연결
        setContentView(binder.root)

        //driver 모델에 config 의존성 주입
        ttsDriver.setConfiguration(TTSConfig())

        //뷰와 이벤트 설정
        linkSpinnerWithAdapter() //언어 선택 스피너 뷰 관련 설정
        initSettingValuesUI() //설정 값을 나타내는 뷰들 내용에 기본 값 설정
        initListeners() //나머지 터치 가능 뷰들의 리스너 설정
    }

    /* onDestroy()에서 tts 객체를 완전 제거 */
    override fun onDestroy() {
        ttsDriver.destroy()
        super.onDestroy()
    }


    //--------------------------------------------
    // 함수 영역 (뷰 설정)
    //

    /* 스피너와 이를 위한 어댑터를 연결하는 함수 */
    private fun linkSpinnerWithAdapter() {
        //language 배열을 가져와 어댑터를 설정하고, 그 어댑터를 스피너와 연결
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        binder.spinnerLanguage.adapter = spinnerAdapter
    }

    /* 기본 pitch, rate, language 받아와서 뷰에 적용 */
    private fun initSettingValuesUI(){
        //pitch 텍스트 설정
        binder.tvCurrentPitch.text = ttsDriver.initPitch().toString()

        //rate 텍스트 설정
        binder.tvCurrentRate.text = ttsDriver.initRate().toString()

        //언어 스피너 값 설정
        binder.spinnerLanguage.setSelection(languages.indexOf(ttsDriver.initLanguage()))
    }

    /* 버튼과 seekBar 관련 이벤트를 설정하는 함수 */
    private fun initListeners(){
        //스피너 아이템 선택 리스너 설정 (선택에 따라 tts 언어를 설정함)
        binder.spinnerLanguage.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ttsDriver.changeLanguage(languages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //btnList 버튼을 누르면 저장된 음성파일 리스트 화면으로 넘어감
        binder.btnList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        //btnSpeak 버튼을 누르면 내용을 바로 읽음
        binder.btnSpeak.setOnClickListener {
            ttsDriver.speak(binder.etTts.text.toString())
        }

        //btnStore 버튼을 누르면 내용을 음성 파일로 저장함
        binder.btnStore.setOnClickListener {
            //파일 제목과 내용 준비(뷰에서부터)
            val title = binder.etTitle.text.toString()
            val contents = binder.etTts.text.toString()

            //제목이 입력되어 있지 않다면 제목 입력 유도, 아니라면 파일 저장 시도
            if(title == "")
                Toast.makeText(this, "파일 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            else
                tryDownLoad(contents, title)
        }

        //seekbarPitch 바를 통해 음성의 pitch(발성)를 높이거나 낮춤
        binder.seekbarPitch.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                applyPitch(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //seekbarRate 바를 통해 음성의 빠르기를 높이거나 낮춤
        binder.seekbarRate.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                applyRate(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    //--------------------------------------------
    // 함수 영역 (기능 관련 내부 함수)
    //

    /* 변경된 음성의 pitch 값을 뷰와 tts 객체에 각각 적용하는 함수들 */
    private fun applyPitch(value: Int){
        val pitch = ttsDriver.changePitch(value)
        binder.tvCurrentPitch.text = pitch.toString()
    }

    /* 변경된 음성의 rate 값을 뷰와 tts 객체에 각각 적용하는 함수들 */
    private fun applyRate(value: Int){
        val rate = ttsDriver.changeRate(value)
        binder.tvCurrentRate.text = rate.toString()
    }

    /* contents 내용으로 만든 음성을 title 이름의 파일로 저장을 시도하는 함수 */
    private fun tryDownLoad(contents: String, title: String){
        //fileManager 객체로 파일을 만들고 driver 객체가 음성 파일 주입 시도함
        val file = ttsFileManager.makeFile("$title.mp3")
        val tryingResult = ttsDriver.insertSpeechInFile(contents, file)

        //성공 여부에 따라 다른 토스트 메시지 출력
        if(tryingResult == TextToSpeech.SUCCESS)
            Toast.makeText(this, "저장 성공", Toast.LENGTH_SHORT).show()
        else {
            ttsFileManager.removeFile(file.name) //음성 주입에 실패했으므로 빈 파일은 삭제
            Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show()
        }
    }
}