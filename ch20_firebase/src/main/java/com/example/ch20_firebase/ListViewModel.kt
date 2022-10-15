package com.example.ch20_firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


//viewModel은 repo에 있는 데이터를 관찰하고 있다가 변경이 되면 mutableData의 값을 변경시켜주는 역할을 합니다.
class ListViewModel: ViewModel() {
    private val repo = Repo()
    fun fetchData(): LiveData<MutableList<User>> {
        val mutableData = MutableLiveData<MutableList<User>>()
        repo.getData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }
}