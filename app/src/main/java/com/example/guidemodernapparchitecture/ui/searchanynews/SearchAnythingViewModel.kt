package com.example.guidemodernapparchitecture.ui.searchanynews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemodernapparchitecture.domain.SearchAnythingUseCase
import com.example.guidemodernapparchitecture.data.models.ApiResult
import com.example.guidemodernapparchitecture.ui.models.NewsUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAnythingViewModel @Inject constructor(
    private val searchAnythingUseCase: SearchAnythingUseCase
): ViewModel() {

    private val _uiState= MutableStateFlow(SearchAnythingUiState())
    val searchAnythingUI: StateFlow<SearchAnythingUiState> = _uiState.asStateFlow()

    private var job: Job? = null

    fun search(searchKeyWord: String) {
        job?.cancel()
        job = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)}
            val result = searchAnythingUseCase.invoke(searchKeyWord)
            when(result) {
                is ApiResult.Success<*> -> {
                    val newsList = result.data as List<NewsUi>
                    _uiState.update { it.copy(isLoading = false, newsList = newsList) }
                }
                is ApiResult.Error -> {}
            }

        }
    }
}