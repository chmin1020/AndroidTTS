package com.example.androidtts.model.fileManager

import java.io.File

/**
 * fileManager 모델을 전체 프로세스에서 하나만 사용하기 위해(singleton) 만든 object
 * 어떤 화면에서든 fileManager 인스턴스는 이 오브젝트의 getInstance()를 통해서 가져온다.
 * 이와 관련하여 provider 내부에서 인스턴스는 딱 1번만 생성이 되도록 설정되어 있다.
 * 다른 환경을 위한 fileManager 인스턴스 제공이 필요하다면 get 함수를 추가하면 된다.
 */
object FileManagerProvider {
    //이 오브젝트가 제공하는 파일 매니저 인스턴스, 한 프로세스 당 한번만 세부 객체가 결정되고 메모리가 할당됨
    private var FILE_MANAGER_INSTANCE: ITTSFileManager? = null

    /* 안드로이드 환경을 위한 fileManager 인스턴스를 가져오기 위해 호출하는 함수 */
    fun getInstanceForAOS(file: File): ITTSFileManager {
        synchronized(FileManagerProvider) {
            if (FILE_MANAGER_INSTANCE == null)
                FILE_MANAGER_INSTANCE = TTSFileManagerForAOS(file)
            return FILE_MANAGER_INSTANCE!!
        }
    }
}