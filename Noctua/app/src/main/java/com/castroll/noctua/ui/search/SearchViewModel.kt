package com.castroll.noctua.ui.search

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castroll.noctua.data.remote.model.User
import com.castroll.noctua.data.remote.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel : ViewModel() {
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private val _allUsers = MutableLiveData<List<User>>()
    val allUsers: LiveData<List<User>> = _allUsers

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> = _currentPage

    private val _totalPages = MutableLiveData<Int>()
    val totalPages: LiveData<Int> = _totalPages

    val pageSize = 7

    init {
        _currentPage.value = 0
    }

    fun fetchUsers(currentUsername: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.getUsers()
                if (response.isSuccessful) {
                    val allUsers = response.body()?.users ?: emptyList()
                    val filteredUsers = allUsers.filter {
                        it.username != currentUsername && it.visible == "true"
                    }
                    _allUsers.value = filteredUsers
                    updateTotalPages()
                    updateCurrentPageUsers()
                } else {
                    //Log.e("SearchViewModel", "Error fetching users: ${response.message()}")
                }
            } catch (e: IOException) {
                //Log.e("SearchViewModel", "Network error: ${e.message}")
            } catch (@SuppressLint("NewApi", "LocalSuppress") e: HttpException) {
                //Log.e("SearchViewModel", "HTTP error: ${e.message}")
            } catch (e: Exception) {
                //Log.e("SearchViewModel", "Unexpected error: ${e.message}")
            }
        }
    }

    fun search(query: String) {
        _allUsers.value = _allUsers.value?.filter {
            it.name.contains(query, ignoreCase = true) || it.username.contains(query, ignoreCase = true)
        }
        _currentPage.value = 0
        updateTotalPages()
        updateCurrentPageUsers()
    }

    fun restoreList(currentUsername: String) {
        fetchUsers(currentUsername)
    }

    fun nextPage() {
        val totalPages = _totalPages.value ?: 0
        if (_currentPage.value!! < totalPages) {
            _currentPage.value = _currentPage.value!! + 1
            updateCurrentPageUsers()
        }
    }

    fun previousPage() {
        if (_currentPage.value!! > 0) {
            _currentPage.value = _currentPage.value!! - 1
            updateCurrentPageUsers()
        }
    }

    private fun updateCurrentPageUsers() {
        val fromIndex = _currentPage.value!! * pageSize
        val toIndex = minOf(fromIndex + pageSize, _allUsers.value?.size ?: 0)
        _users.value = _allUsers.value?.subList(fromIndex, toIndex)
    }

    private fun updateTotalPages() {
        _totalPages.value = (_allUsers.value?.size ?: 0) / pageSize
    }
}










