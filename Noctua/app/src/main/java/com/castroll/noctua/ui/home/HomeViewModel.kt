package com.castroll.noctua.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castroll.noctua.data.remote.model.News
import com.castroll.noctua.data.remote.repository.NewsRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _News = MutableLiveData<List<News>>()
    val news: LiveData<List<News>> = _News

    init {
        fetchNews()
    }
    private fun fetchNews(){
        viewModelScope.launch {
            try {
                val fetchedNews = NewsRepository().fetchNews()
                _News.value = fetchedNews
            } catch (_: Exception){

            }
        }
    }


    private val _text = MutableLiveData<String>().apply {
        value = "Home Screen"
    }
    val text: LiveData<String> = _text
}
