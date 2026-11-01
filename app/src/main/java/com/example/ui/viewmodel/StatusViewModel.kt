package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.data.datastore.PreferencesManager
import com.example.data.model.MediaFilter
import com.example.data.model.StatusItem
import com.example.data.model.StatusSource
import com.example.data.repository.MediaRepository
import com.example.worker.AutoSaveWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class StatusUiState(
    val selectedTab: StatusSource = StatusSource.WHATSAPP,
    val selectedFilter: MediaFilter = MediaFilter.ALL,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isAutoSaveEnabled: Boolean = false,
    val autoSaveIntervalHours: Int = 1,
    val isBusinessEnabled: Boolean = false,
    val whatsappUri: Uri? = null,
    val businessUri: Uri? = null,
    val whatsappItems: List<StatusItem> = emptyList(),
    val businessItems: List<StatusItem> = emptyList(),
    val savedVaultItems: List<StatusItem> = emptyList(),
    val selectedMediaItem: StatusItem? = null,
    val selectedItemIds: Set<String> = emptySet(),
    val userMessage: String? = null,
    val isSettingsSheetOpen: Boolean = false,
    val isFolderGuideOpen: Boolean = false,
    val isGridView: Boolean = true,
    val isInitialLoading: Boolean = true
) {
    val isSelectionMode: Boolean
        get() = selectedItemIds.isNotEmpty()

    val selectedCount: Int
        get() = selectedItemIds.size
    val activeTabs: List<StatusSource>
        get() = if (isBusinessEnabled) {
            listOf(StatusSource.WHATSAPP, StatusSource.BUSINESS, StatusSource.SAVED_VAULT)
        } else {
            listOf(StatusSource.WHATSAPP, StatusSource.SAVED_VAULT)
        }

    val currentTabItems: List<StatusItem>
        get() = when (selectedTab) {
            StatusSource.WHATSAPP -> whatsappItems
            StatusSource.BUSINESS -> businessItems
            StatusSource.SAVED_VAULT -> savedVaultItems
        }

    val filteredItems: List<StatusItem>
        get() = currentTabItems.filter { item ->
            when (selectedFilter) {
                MediaFilter.ALL -> true
                MediaFilter.IMAGES -> !item.isVideo
                MediaFilter.VIDEOS -> item.isVideo
            }
        }

    val isCurrentFolderGranted: Boolean
        get() = when (selectedTab) {
            StatusSource.WHATSAPP -> whatsappUri != null
            StatusSource.BUSINESS -> businessUri != null
            StatusSource.SAVED_VAULT -> true
        }
}

class StatusViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "StatusViewModel"
    }

    private val repository = MediaRepository(application)
    private val prefs = PreferencesManager(application)
    private val workManager = WorkManager.getInstance(application)

    private val _uiState = MutableStateFlow(StatusUiState())
    val uiState: StateFlow<StatusUiState> = _uiState.asStateFlow()

    init {
        // Collect persisted settings from DataStore
        viewModelScope.launch {
            combine(
                prefs.whatsappTreeUri,
                prefs.businessTreeUri,
                prefs.isAutoSaveEnabled,
                prefs.autoSaveIntervalHours,
                prefs.isBusinessEnabled
            ) { waUriStr, bizUriStr, autoSave, interval, bizEnabled ->
                val waUri = waUriStr?.let { Uri.parse(it) }
                val bizUri = bizUriStr?.let { Uri.parse(it) }
                _uiState.update { current ->
                    current.copy(
                        whatsappUri = waUri,
                        businessUri = bizUri,
                        isAutoSaveEnabled = autoSave,
                        autoSaveIntervalHours = interval,
                        isBusinessEnabled = bizEnabled
                    )
                }
            }.collect {
                refreshAllStatuses(isSilent = true)
            }
        }
    }

    fun selectTab(source: StatusSource) {
        _uiState.update { it.copy(selectedTab = source, selectedItemIds = emptySet()) }
    }

    fun selectFilter(filter: MediaFilter) {
        _uiState.update { it.copy(selectedFilter = filter, selectedItemIds = emptySet()) }
    }

    fun openMediaViewer(item: StatusItem) {
        _uiState.update { it.copy(selectedMediaItem = item) }
    }

    fun closeMediaViewer() {
        _uiState.update { it.copy(selectedMediaItem = null) }
    }

    fun setSettingsSheetOpen(open: Boolean) {
        _uiState.update { it.copy(isSettingsSheetOpen = open) }
    }

    fun setFolderGuideOpen(open: Boolean) {
        _uiState.update { it.copy(isFolderGuideOpen = open) }
    }

    fun setGridView(isGrid: Boolean) {
        _uiState.update { it.copy(isGridView = isGrid) }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun onFolderPermissionGranted(source: StatusSource, uri: Uri) {
        viewModelScope.launch {
            val uriStr = uri.toString()
            if (source == StatusSource.WHATSAPP) {
                prefs.setWhatsAppTreeUri(uriStr)
            } else if (source == StatusSource.BUSINESS) {
                prefs.setBusinessTreeUri(uriStr)
            }
            _uiState.update {
                it.copy(
                    userMessage = "${source.label} folder permission linked successfully!"
                )
            }
            refreshAllStatuses(isSilent = false)
        }
    }

    fun refreshAllStatuses(isSilent: Boolean = false) {
        viewModelScope.launch {
            if (!isSilent) {
                _uiState.update { it.copy(isRefreshing = true) }
            }

            try {
                // 1. Load saved vault items first to accurately tag isSaved on other items
                val savedItems = repository.loadSavedVaultStatuses()

                // 2. Load WhatsApp statuses if URI is configured
                val waUri = _uiState.value.whatsappUri
                val waItems = if (waUri != null) {
                    repository.loadStatusesFromTree(waUri, StatusSource.WHATSAPP, savedItems)
                } else {
                    emptyList()
                }

                // 3. Load WA Business statuses if URI is configured
                val bizUri = _uiState.value.businessUri
                val bizItems = if (bizUri != null) {
                    repository.loadStatusesFromTree(bizUri, StatusSource.BUSINESS, savedItems)
                } else {
                    emptyList()
                }

                _uiState.update {
                    it.copy(
                        savedVaultItems = savedItems,
                        whatsappItems = waItems,
                        businessItems = bizItems,
                        isRefreshing = false,
                        isLoading = false,
                        isInitialLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error refreshing statuses", e)
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        isLoading = false,
                        isInitialLoading = false,
                        userMessage = "Could not refresh statuses: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun saveStatus(item: StatusItem) {
        viewModelScope.launch {
            val result = repository.saveStatusToGallery(item)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        userMessage = "Status Saved",
                        selectedMediaItem = it.selectedMediaItem?.let { selected ->
                            if (selected.id == item.id) selected.copy(isSaved = true) else selected
                        }
                    )
                }
                refreshAllStatuses(isSilent = true)
            } else {
                _uiState.update {
                    it.copy(userMessage = "Failed to save: ${result.exceptionOrNull()?.message}")
                }
            }
        }
    }

    fun deleteSavedItem(item: StatusItem, nextItem: StatusItem? = null) {
        viewModelScope.launch {
            val success = repository.deleteSavedStatus(item)
            if (success) {
                _uiState.update {
                    it.copy(
                        userMessage = "Status Removed",
                        selectedMediaItem = nextItem
                    )
                }
                refreshAllStatuses(isSilent = true)
            } else {
                _uiState.update {
                    it.copy(userMessage = "Failed to delete item")
                }
            }
        }
    }

    fun toggleItemSelection(itemId: String) {
        _uiState.update { current ->
            val updated = if (current.selectedItemIds.contains(itemId)) {
                current.selectedItemIds - itemId
            } else {
                current.selectedItemIds + itemId
            }
            current.copy(selectedItemIds = updated)
        }
    }

    fun selectAll() {
        _uiState.update { current ->
            val allIds = current.filteredItems.map { it.id }.toSet()
            val newSelected = if (current.selectedItemIds.containsAll(allIds)) {
                emptySet()
            } else {
                allIds
            }
            current.copy(selectedItemIds = newSelected)
        }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedItemIds = emptySet()) }
    }

    fun batchSaveSelected() {
        val selectedIds = _uiState.value.selectedItemIds
        if (selectedIds.isEmpty()) return
        val itemsToSave = _uiState.value.currentTabItems.filter { it.id in selectedIds && !it.isSaved }
        if (itemsToSave.isEmpty()) {
            _uiState.update {
                it.copy(
                    userMessage = "Selected statuses are already saved",
                    selectedItemIds = emptySet()
                )
            }
            return
        }

        viewModelScope.launch {
            var savedCount = 0
            itemsToSave.forEach { item ->
                val result = repository.saveStatusToGallery(item)
                if (result.isSuccess) {
                    savedCount++
                }
            }
            _uiState.update {
                it.copy(
                    userMessage = "Saved $savedCount status${if (savedCount > 1) "es" else ""}",
                    selectedItemIds = emptySet()
                )
            }
            refreshAllStatuses(isSilent = true)
        }
    }

    fun batchDeleteSelected() {
        val selectedIds = _uiState.value.selectedItemIds
        if (selectedIds.isEmpty()) return
        val itemsToDelete = _uiState.value.savedVaultItems.filter { it.id in selectedIds }
        if (itemsToDelete.isEmpty()) return

        viewModelScope.launch {
            var deletedCount = 0
            itemsToDelete.forEach { item ->
                if (repository.deleteSavedStatus(item)) {
                    deletedCount++
                }
            }
            _uiState.update {
                it.copy(
                    userMessage = "Deleted $deletedCount status${if (deletedCount > 1) "es" else ""}",
                    selectedItemIds = emptySet()
                )
            }
            refreshAllStatuses(isSilent = true)
        }
    }

    fun shareItem(item: StatusItem) {
        repository.shareMediaItem(item)
    }

    fun toggleBusinessEnabled(enabled: Boolean) {
        viewModelScope.launch {
            prefs.setBusinessEnabled(enabled)
            _uiState.update { current ->
                val newSelected = if (!enabled && current.selectedTab == StatusSource.BUSINESS) {
                    StatusSource.WHATSAPP
                } else current.selectedTab
                current.copy(
                    isBusinessEnabled = enabled,
                    selectedTab = newSelected,
                    userMessage = if (enabled) "WA Business tab enabled" else "WA Business tab hidden"
                )
            }
        }
    }

    fun toggleAutoSave(enabled: Boolean) {
        viewModelScope.launch {
            prefs.setAutoSaveEnabled(enabled)
            _uiState.update { it.copy(isAutoSaveEnabled = enabled) }

            if (enabled) {
                scheduleAutoSaveWorker(_uiState.value.autoSaveIntervalHours)
                _uiState.update {
                    it.copy(userMessage = "Auto-Save activated! Statuses will sync hourly.")
                }
            } else {
                workManager.cancelUniqueWork(AutoSaveWorker.WORK_NAME)
                _uiState.update {
                    it.copy(userMessage = "Auto-Save disabled.")
                }
            }
        }
    }

    fun runAutoSaveNow() {
        viewModelScope.launch {
            val oneTimeRequest = OneTimeWorkRequestBuilder<AutoSaveWorker>().build()
            workManager.enqueue(oneTimeRequest)
            _uiState.update { it.copy(userMessage = "Background sync initiated...") }
        }
    }

    private fun scheduleAutoSaveWorker(intervalHours: Int) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<AutoSaveWorker>(
            intervalHours.toLong().coerceAtLeast(1L),
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            AutoSaveWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWork
        )
        Log.i(TAG, "Scheduled PeriodicWorkRequest for AutoSaveWorker with interval ${intervalHours}h")
    }
}
