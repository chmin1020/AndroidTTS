package com.example.androidtts.model.fileManager

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

/**
 * 안드로이드 환경에서 FIleManager 역할을 할 모델 객체.
 * 앱 콘텍스트를 인자로 받아서 파일 관련 비즈니스 로직을 처리한다.
 **/
class TTSFileManagerForAOS(private val context: Context): ITTSFileManager {

    private val savedPath = context.filesDir

    /* 현재 저장된 음성 파일 이름들을 리스트로 전달하는 함수 */
    override fun getFileNameList(): List<String> {
        val fileNames = mutableListOf<String>()

        //확장자가 mp3인 파일만 뽑아서 리스트에 추가 (tts 관련 파일만 가져오기 위함)
        val mp3Files = savedPath.listFiles()?.filter { it.extension == "mp3" }
        mp3Files?.forEach { fileNames.add(it.name) }

        return fileNames
    }

    /* 새로운 음성 파일을 추가하고자 할 때 호출하는 함수 */
    override fun makeFile(fileName: String):  File {
        return File(savedPath, fileName)
    }

    /* 선택한 음성 파일을 실행하는 함수 */
    override fun playFile(fileName: String) {
        val targetFile = File(savedPath, fileName)
        MediaPlayer.create(context, targetFile.toUri()).start()
    }

    /* 기존 음성 파일 중 하나를 삭제하고자 할 때 호출하는 함수 */
    override fun removeFile(fileName: String) {
        val targetFile = File(savedPath, fileName)
        targetFile.delete()
    }
}