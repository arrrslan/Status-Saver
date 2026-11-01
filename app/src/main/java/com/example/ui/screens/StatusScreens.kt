package com.example.ui.screens

import com.example.ui.theme.*
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.example.data.model.MediaFilter
import com.example.data.model.StatusItem
import com.example.data.model.StatusSource
import com.example.data.repository.MediaRepository
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceHighlight
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintAccentBright
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StatusUiState
import com.example.ui.viewmodel.StatusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusMainScreen(
    viewModel: StatusViewModel,
    onRequestWhatsAppPermission: () -> Unit,
    onRequestBusinessPermission: () -> Unit,
    onRequestNotificationPermission: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var showBatchDeleteDialog by remember { mutableStateOf(false) }
    var singleItemToDelete by remember { mutableStateOf<StatusItem?>(null) }

    // Intercept back button to exit multi-select mode if active
    BackHandler(enabled = uiState.isSelectionMode) {
        viewModel.clearSelection()
    }

    // Video-frame-enabled Coil image loader
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }

    // Floating animated toaster message handler
    LaunchedEffect(uiState.userMessage) {
        val message = uiState.userMessage
        if (message != null) {
            toastMessage = message
            delay(1200L)
            toastMessage = null
            viewModel.clearUserMessage()
        }
    }

    val activeTabs = uiState.activeTabs
    val selectedTabIndex = remember(activeTabs, uiState.selectedTab) {
        val idx = activeTabs.indexOf(uiState.selectedTab)
        if (idx >= 0) idx else 0
    }
    val tabPagerState = rememberPagerState(
        initialPage = selectedTabIndex.coerceIn(0, (activeTabs.size - 1).coerceAtLeast(0)),
        pageCount = { activeTabs.size }
    )

    // Synchronize tab pager when tab selection changes externally or tab list shrinks
    LaunchedEffect(selectedTabIndex, activeTabs.size) {
        if (activeTabs.isNotEmpty()) {
            val targetPage = selectedTabIndex.coerceIn(0, activeTabs.size - 1)
            if (tabPagerState.currentPage != targetPage) {
                tabPagerState.animateScrollToPage(targetPage)
            }
        }
    }

    // Synchronize ViewModel tab selection when user swipes the pager
    LaunchedEffect(tabPagerState.currentPage, activeTabs) {
        val targetTab = activeTabs.getOrNull(tabPagerState.currentPage)
        if (targetTab != null && targetTab != uiState.selectedTab) {
            viewModel.selectTab(targetTab)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                StatusTopAppBar(
                    isRefreshing = uiState.isRefreshing,
                    totalAvailable = uiState.whatsappItems.size + uiState.businessItems.size,
                    totalSaved = uiState.savedVaultItems.size,
                    onRefresh = { viewModel.refreshAllStatuses() },
                    onOpenSettings = { viewModel.setSettingsSheetOpen(true) },
                    onOpenGuide = { viewModel.setFolderGuideOpen(true) }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Sub-Filters: All | Photos | Videos + Grid / List View Mode Toggle
                FilterChipsSection(
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = { viewModel.selectFilter(it) },
                    isGridView = uiState.isGridView,
                    onViewModeChanged = { viewModel.setGridView(it) },
                    totalItemsCount = uiState.filteredItems.size
                )

                // Content Area with Tab Swipes
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    HorizontalPager(
                        state = tabPagerState,
                        beyondViewportPageCount = 1,
                        modifier = Modifier.fillMaxSize(),
                        key = { activeTabs.getOrNull(it)?.name ?: "tab_$it" }
                    ) { pageIndex ->
                        val pageSource = activeTabs.getOrNull(pageIndex) ?: return@HorizontalPager
                        val pageItems = when (pageSource) {
                            StatusSource.WHATSAPP -> uiState.whatsappItems
                            StatusSource.BUSINESS -> uiState.businessItems
                            StatusSource.SAVED_VAULT -> uiState.savedVaultItems
                        }.let { list ->
                            when (uiState.selectedFilter) {
                                MediaFilter.ALL -> list
                                MediaFilter.IMAGES -> list.filter { !it.isVideo }
                                MediaFilter.VIDEOS -> list.filter { it.isVideo }
                            }
                        }

                        val isGranted = when (pageSource) {
                            StatusSource.WHATSAPP -> uiState.whatsappUri != null
                            StatusSource.BUSINESS -> uiState.businessUri != null
                            StatusSource.SAVED_VAULT -> true
                        }

                        if (uiState.isInitialLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        } else if (!isGranted) {
                            PermissionPromptCard(
                                source = pageSource,
                                onGrantClicked = {
                                    if (pageSource == StatusSource.WHATSAPP) {
                                        onRequestWhatsAppPermission()
                                    } else {
                                        onRequestBusinessPermission()
                                    }
                                },
                                onHelpClicked = { viewModel.setFolderGuideOpen(true) }
                            )
                        } else if (pageItems.isEmpty()) {
                            EmptyStatusView(
                                source = pageSource,
                                filter = uiState.selectedFilter,
                                onRefresh = { viewModel.refreshAllStatuses() }
                            )
                        } else if (uiState.isGridView) {
                            StatusMediaGrid(
                                items = pageItems,
                                imageLoader = imageLoader,
                                isSelectionMode = uiState.isSelectionMode,
                                selectedItemIds = uiState.selectedItemIds,
                                onItemClick = { item ->
                                    if (uiState.isSelectionMode) {
                                        viewModel.toggleItemSelection(item.id)
                                    } else {
                                        viewModel.openMediaViewer(item)
                                    }
                                },
                                onItemLongClick = { item ->
                                    viewModel.toggleItemSelection(item.id)
                                },
                                onSaveClick = { viewModel.saveStatus(it) },
                                onDeleteClick = { singleItemToDelete = it },
                                onShareClick = { viewModel.shareItem(it) }
                            )
                        } else {
                            StatusMediaList(
                                items = pageItems,
                                imageLoader = imageLoader,
                                isSelectionMode = uiState.isSelectionMode,
                                selectedItemIds = uiState.selectedItemIds,
                                onItemClick = { item ->
                                    if (uiState.isSelectionMode) {
                                        viewModel.toggleItemSelection(item.id)
                                    } else {
                                        viewModel.openMediaViewer(item)
                                    }
                                },
                                onItemLongClick = { item ->
                                    viewModel.toggleItemSelection(item.id)
                                },
                                onSaveClick = { viewModel.saveStatus(it) },
                                onDeleteClick = { singleItemToDelete = it },
                                onShareClick = { viewModel.shareItem(it) }
                            )
                        }
                    }

                    // Refreshing spinner overlay
                    if (uiState.isRefreshing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Area: Animated between regular Tabs and Contextual Selection Dock
        AnimatedVisibility(
            visible = !uiState.isSelectionMode,
            enter = fadeIn(animationSpec = tween(180)) + slideInVertically(animationSpec = tween(180)) { it / 2 },
            exit = fadeOut(animationSpec = tween(150)) + slideOutVertically(animationSpec = tween(150)) { it / 2 },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
                .zIndex(50f)
        ) {
            StatusTabsSection(
                activeTabs = activeTabs,
                selectedTab = uiState.selectedTab,
                whatsappCount = uiState.whatsappItems.size,
                businessCount = uiState.businessItems.size,
                vaultCount = uiState.savedVaultItems.size,
                onTabSelected = { source, index ->
                    coroutineScope.launch {
                        tabPagerState.animateScrollToPage(index)
                    }
                    viewModel.selectTab(source)
                }
            )
        }

        // Contextual Multi-Select Bottom Dock
        AnimatedVisibility(
            visible = uiState.isSelectionMode,
            enter = fadeIn(animationSpec = tween(200)) + slideInVertically(animationSpec = tween(200)) { it / 2 },
            exit = fadeOut(animationSpec = tween(160)) + slideOutVertically(animationSpec = tween(160)) { it / 2 },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
                .zIndex(60f)
        ) {
            SelectionBottomDock(
                selectedCount = uiState.selectedCount,
                totalCount = uiState.filteredItems.size,
                isSavedTab = uiState.selectedTab == StatusSource.SAVED_VAULT,
                onSelectAll = { viewModel.selectAll() },
                onClearSelection = { viewModel.clearSelection() },
                onAction = {
                    if (uiState.selectedTab == StatusSource.SAVED_VAULT) {
                        showBatchDeleteDialog = true
                    } else {
                        viewModel.batchSaveSelected()
                    }
                }
            )
        }

        // Bottom Floating Animated Monochrome Pill Toaster positioned above the bottom pill bar
        BottomCapsuleToaster(
            message = toastMessage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 80.dp)
                .zIndex(90f)
        )
    }

    // Full-Screen Media Viewer with Fluid Zoom Expansion File Opening Animation
    AnimatedVisibility(
        visible = uiState.selectedMediaItem != null,
        enter = fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)) +
                scaleIn(initialScale = 0.82f, animationSpec = tween(280, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(220, easing = FastOutLinearInEasing)) +
               scaleOut(targetScale = 0.85f, animationSpec = tween(220, easing = FastOutLinearInEasing)),
        modifier = Modifier.zIndex(100f)
    ) {
        uiState.selectedMediaItem?.let { item ->
            FullScreenMediaViewer(
                initialItem = item,
                items = uiState.filteredItems,
                imageLoader = imageLoader,
                onDismiss = { viewModel.closeMediaViewer() },
                onSave = { itemToSave -> viewModel.saveStatus(itemToSave) },
                onDelete = { itemToDelete, nextItem -> viewModel.deleteSavedItem(itemToDelete, nextItem) },
                onShare = { itemToShare -> viewModel.shareItem(itemToShare) }
            )
        }
    }

    // Batch Delete Confirmation Dialog
    if (showBatchDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showBatchDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text(
                    text = "Delete ${uiState.selectedCount} Statuses?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "This will permanently remove the selected statuses from your saved collection and device storage.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBatchDeleteDialog = false
                        viewModel.batchDeleteSelected()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.testTag("confirm_batch_delete_button")
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBatchDeleteDialog = false },
                    modifier = Modifier.testTag("cancel_batch_delete_button")
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Single Item Delete Confirmation Dialog
    singleItemToDelete?.let { itemToDelete ->
        AlertDialog(
            onDismissRequest = { singleItemToDelete = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text(
                    text = "Delete Saved Status?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this status? This will permanently remove the file from your saved collection and device storage.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = itemToDelete
                        singleItemToDelete = null
                        viewModel.deleteSavedItem(target)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.testTag("confirm_single_delete_button")
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { singleItemToDelete = null },
                    modifier = Modifier.testTag("cancel_single_delete_button")
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Settings Bottom Sheet
    if (uiState.isSettingsSheetOpen) {
        SettingsBottomSheet(
            uiState = uiState,
            onDismiss = { viewModel.setSettingsSheetOpen(false) },
            onToggleAutoSave = { enabled ->
                if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    onRequestNotificationPermission()
                }
                viewModel.toggleAutoSave(enabled)
            },
            onToggleBusiness = { viewModel.toggleBusinessEnabled(it) },
            onRunSyncNow = { viewModel.runAutoSaveNow() },
            onOpenGuide = {
                viewModel.setSettingsSheetOpen(false)
                viewModel.setFolderGuideOpen(true)
            }
        )
    }

    // Folder Guide Dialog
    if (uiState.isFolderGuideOpen) {
        FolderGuideDialog(
            onDismiss = { viewModel.setFolderGuideOpen(false) }
        )
    }
}

@Composable
fun BottomCapsuleToaster(
    message: String?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut(),
        modifier = modifier
    ) {
        message?.let { text ->
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFFFAFAFA),
                border = BorderStroke(1.dp, Color.White),
                shadowElevation = 14.dp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .testTag("status_toaster")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF09090B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.3.sp
                        ),
                        color = Color(0xFF09090B)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusTopAppBar(
    isRefreshing: Boolean,
    totalAvailable: Int = 0,
    totalSaved: Int = 0,
    onRefresh: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenGuide: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Simpler Black Download App Icon with clean white download arrow and tray
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black)
                            .border(1.5.dp, MintAccent.copy(alpha = 0.45f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Status Saver Logo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Status Saver",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.3).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (totalAvailable > 0 || totalSaved > 0) {
                                "$totalAvailable available • $totalSaved saved"
                            } else {
                                "WhatsApp & WA Business Statuses"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Modern sleek borderless action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier
                            .size(42.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("refresh_button")
                    ) {
                        val rotation by animateFloatAsState(
                            targetValue = if (isRefreshing) 360f else 0f,
                            animationSpec = if (isRefreshing) {
                                infiniteRepeatable(
                                    animation = tween(800, easing = LinearEasing),
                                    repeatMode = RepeatMode.Restart
                                )
                            } else {
                                tween(300)
                            },
                            label = "refreshRotation"
                        )
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Statuses",
                            tint = if (isRefreshing) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(22.dp)
                                .graphicsLayer(rotationZ = rotation)
                        )
                    }

                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(42.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open Settings",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusTabsSection(
    activeTabs: List<StatusSource>,
    selectedTab: StatusSource,
    whatsappCount: Int,
    businessCount: Int,
    vaultCount: Int,
    onTabSelected: (StatusSource, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = remember(activeTabs, selectedTab) {
        activeTabs.indexOf(selectedTab).coerceAtLeast(0)
    }
    val tabCount = activeTabs.size.coerceAtLeast(1)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFF18181B),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
            shadowElevation = 10.dp,
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .height(54.dp)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                val tabWidth = maxWidth / tabCount
                val animatedPillOffset by animateDpAsState(
                    targetValue = tabWidth * selectedIndex,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioLowBouncy
                    ),
                    label = "tabPillOffset"
                )

                // Smoothly sliding active pill background
                Box(
                    modifier = Modifier
                        .offset(x = animatedPillOffset)
                        .width(tabWidth)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color(0xFF27272A))
                        .border(1.2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(26.dp))
                )

                // Clickable tab items row overlay
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    activeTabs.forEachIndexed { index, source ->
                        val isSelected = selectedTab == source
                        val count = when (source) {
                            StatusSource.WHATSAPP -> whatsappCount
                            StatusSource.BUSINESS -> businessCount
                            StatusSource.SAVED_VAULT -> vaultCount
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(26.dp))
                                .clickable { onTabSelected(source, index) }
                                .testTag("tab_${source.name.lowercase(Locale.ROOT)}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = source.label,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        letterSpacing = 0.2.sp
                                    ),
                                    color = if (isSelected) Color.White else Color(0xFFA1A1AA),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (count > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(if (isSelected) Color.White else Color(0xFF27272A))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = count.toString(),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipsSection(
    selectedFilter: MediaFilter,
    onFilterSelected: (MediaFilter) -> Unit,
    isGridView: Boolean,
    onViewModeChanged: (Boolean) -> Unit,
    totalItemsCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Filter Chips (All | Photos | Videos)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MediaFilter.values().forEach { filter ->
                val isSelected = filter == selectedFilter
                val chipBg by animateColorAsState(
                    targetValue = if (isSelected) Color(0xFF27272A) else Color(0xFF18181B),
                    label = "chipBg"
                )
                val chipContent by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color(0xFFA1A1AA),
                    label = "chipContent"
                )
                val border = if (isSelected) {
                    BorderStroke(1.2.dp, Color.White.copy(alpha = 0.85f))
                } else {
                    BorderStroke(0.8.dp, Color.White.copy(alpha = 0.15f))
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = chipBg,
                    border = border,
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onFilterSelected(filter) }
                        .testTag("filter_${filter.name.lowercase(Locale.ROOT)}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val icon = when (filter) {
                            MediaFilter.ALL -> Icons.Default.GridView
                            MediaFilter.IMAGES -> Icons.Default.Image
                            MediaFilter.VIDEOS -> Icons.Default.Videocam
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = chipContent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = filter.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = chipContent
                        )
                    }
                }
            }
        }

        // Right: View Mode Toggle (Grid vs List)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF18181B),
            border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.20f)),
            modifier = Modifier.height(36.dp)
        ) {
            Row(
                modifier = Modifier.padding(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Grid Toggle Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isGridView) Color(0xFF27272A) else Color.Transparent)
                        .border(
                            width = if (isGridView) 1.dp else 0.dp,
                            color = if (isGridView) Color.White.copy(alpha = 0.6f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onViewModeChanged(true) }
                        .testTag("view_mode_grid"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Grid View",
                        tint = if (isGridView) Color.White else Color(0xFFA1A1AA),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // List Toggle Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (!isGridView) Color(0xFF27272A) else Color.Transparent)
                        .border(
                            width = if (!isGridView) 1.dp else 0.dp,
                            color = if (!isGridView) Color.White.copy(alpha = 0.6f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onViewModeChanged(false) }
                        .testTag("view_mode_list"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewList,
                        contentDescription = "List View",
                        tint = if (!isGridView) Color.White else Color(0xFFA1A1AA),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusMediaGrid(
    items: List<StatusItem>,
    imageLoader: ImageLoader,
    isSelectionMode: Boolean,
    selectedItemIds: Set<String>,
    onItemClick: (StatusItem) -> Unit,
    onItemLongClick: (StatusItem) -> Unit,
    onSaveClick: (StatusItem) -> Unit,
    onDeleteClick: (StatusItem) -> Unit,
    onShareClick: (StatusItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("media_grid")
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            val isSelected = selectedItemIds.contains(item.id)
            StatusMediaCard(
                item = item,
                imageLoader = imageLoader,
                isSelectionMode = isSelectionMode,
                isSelected = isSelected,
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) },
                onSaveClick = { onSaveClick(item) },
                onDeleteClick = { onDeleteClick(item) },
                onShareClick = { onShareClick(item) }
            )
        }
    }
}

@Composable
fun StatusMediaCard(
    item: StatusItem,
    imageLoader: ImageLoader,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSaveClick: (StatusItem) -> Unit,
    onDeleteClick: (StatusItem) -> Unit,
    onShareClick: (StatusItem) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "grid_card_press_scale"
    )

    val cardBorder = if (isSelected) {
        BorderStroke(2.dp, Color.White)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = cardBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .clip(RoundedCornerShape(20.dp))
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .pointerInput(isSelectionMode) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClick()
                    },
                    onLongPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick()
                    }
                )
            }
            .testTag("media_card_${item.name}")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Media Image Thumbnail
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.uri)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // When selected, subtle translucent overlay
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.15f))
                )
            }

            // Centered Play Icon for Videos
            if (item.isVideo) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.60f))
                        .border(1.2.dp, Color.White.copy(alpha = 0.55f), CircleShape)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Video",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Video Duration Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.35f)),
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = formatTimeMs(item.durationMs),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Top Status Overlays (Multi-Select Checkbox on Left, Downloaded Badge on Right)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
            ) {
                // Multi-select Checkbox (Top-Start)
                if (isSelectionMode) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.5f),
                        border = BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .size(26.dp)
                            .align(Alignment.TopStart)
                            .testTag("checkbox_${item.id}")
                    ) {
                        if (isSelected) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color(0xFF09090B),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Multi-Stop Gradient Scrim & Quick Actions
            if (!isSelectionMode) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.Black.copy(alpha = 0.90f)
                                )
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Quick Save / Delete Action Button
                        if (item.source == StatusSource.SAVED_VAULT) {
                            // Quick Delete Button ONLY on Saved Tab (Matching theme)
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF27272A),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.65f)),
                                shadowElevation = 3.dp,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clickable { onDeleteClick(item) }
                                    .testTag("quick_delete_${item.id}")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Status",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        } else if (!item.isSaved) {
                            // Quick Save Button / Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White,
                                shadowElevation = 4.dp,
                                modifier = Modifier
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onSaveClick(item) }
                                    .testTag("quick_save_${item.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Save Status",
                                        tint = Color(0xFF09090B),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Save",
                                        color = Color(0xFF09090B),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            // Already Saved Badge Pill on WhatsApp / Business tab
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF27272A),
                                border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.35f)),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Saved",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Quick Share Button
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.65f),
                            border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable { onShareClick(item) }
                                .testTag("quick_share_${item.id}")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share Status",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusMediaList(
    items: List<StatusItem>,
    imageLoader: ImageLoader,
    isSelectionMode: Boolean,
    selectedItemIds: Set<String>,
    onItemClick: (StatusItem) -> Unit,
    onItemLongClick: (StatusItem) -> Unit,
    onSaveClick: (StatusItem) -> Unit,
    onDeleteClick: (StatusItem) -> Unit,
    onShareClick: (StatusItem) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("media_list")
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            val isSelected = selectedItemIds.contains(item.id)
            StatusMediaRowCard(
                item = item,
                imageLoader = imageLoader,
                isSelectionMode = isSelectionMode,
                isSelected = isSelected,
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) },
                onSaveClick = { onSaveClick(item) },
                onDeleteClick = { onDeleteClick(item) },
                onShareClick = { onShareClick(item) }
            )
        }
    }
}

@Composable
fun StatusMediaRowCard(
    item: StatusItem,
    imageLoader: ImageLoader,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "row_card_press_scale"
    )

    val cardBorder = if (isSelected) {
        BorderStroke(2.dp, Color.White)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = cardBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(RoundedCornerShape(18.dp))
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .pointerInput(isSelectionMode) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClick()
                    },
                    onLongPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick()
                    }
                )
            }
            .testTag("media_row_${item.name}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail container (70.dp x 70.dp)
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.uri)
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.2f))
                    )
                }

                if (item.isVideo) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.65f))
                            .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Video",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (isSelectionMode) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.6f),
                        border = BorderStroke(1.5.dp, Color.White),
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopStart)
                            .padding(2.dp)
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color(0xFF09090B),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center details column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceHighlight,
                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = if (item.isVideo) "VIDEO • ${formatTimeMs(item.durationMs)}" else "PHOTO",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatFileSize(item.size),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right Quick Actions (when not in multi-select mode)
            if (!isSelectionMode) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.source == StatusSource.SAVED_VAULT) {
                        // Quick Delete Button ONLY on Saved Tab
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF27272A),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.65f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { onDeleteClick() }
                                .testTag("list_quick_delete_${item.id}")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Status",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    } else if (!item.isSaved) {
                        // Quick Save Button
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { onSaveClick() }
                                .testTag("list_quick_save_${item.id}")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Save Status",
                                    tint = Color(0xFF09090B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    } else {
                        // Already Saved Badge
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF27272A),
                            border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.35f)),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Saved",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Quick Share Button
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceHighlight,
                        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onShareClick() }
                            .testTag("list_quick_share_${item.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Status",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionBottomDock(
    selectedCount: Int,
    totalCount: Int,
    isSavedTab: Boolean,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
            shadowElevation = 14.dp,
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .height(60.dp)
                .testTag("selection_bottom_dock")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Close selection mode button
                IconButton(
                    onClick = onClearSelection,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("close_selection_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Selection Mode",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Selection count text
                Text(
                    text = "$selectedCount selected",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                // Select All toggle
                TextButton(
                    onClick = onSelectAll,
                    modifier = Modifier.testTag("select_all_button")
                ) {
                    Text(
                        text = if (selectedCount == totalCount && totalCount > 0) "Deselect" else "All",
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                // Primary Batch Action: Save or Delete
                if (isSavedTab) {
                    Button(
                        onClick = onAction,
                        enabled = selectedCount > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ErrorRed,
                            contentColor = Color.White,
                            disabledContainerColor = ErrorRed.copy(alpha = 0.3f),
                            disabledContentColor = Color.White.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("batch_delete_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Delete",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Button(
                        onClick = onAction,
                        enabled = selectedCount > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF09090B),
                            disabledContainerColor = Color.White.copy(alpha = 0.3f),
                            disabledContentColor = Color(0xFF09090B).copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("batch_save_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Save",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionPromptCard(
    source: StatusSource,
    onGrantClicked: () -> Unit,
    onHelpClicked: () -> Unit
) {
    val targetPath = if (source == StatusSource.BUSINESS) {
        MediaRepository.PATH_BUSINESS
    } else {
        MediaRepository.PATH_WHATSAPP
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = "Folder Permission",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Grant ${source.label} Access",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Due to Android Scoped Storage, select and allow access to the hidden .Statuses folder:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = targetPath,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onGrantClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("grant_permission_button")
                ) {
                    Icon(imageVector = Icons.Default.Folder, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Grant Folder Access")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onHelpClicked,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Step-by-step Setup Guide")
                }
            }
        }
    }
}

@Composable
fun EmptyStatusView(
    source: StatusSource,
    filter: MediaFilter,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (filter) {
                    MediaFilter.ALL -> Icons.Default.Image
                    MediaFilter.IMAGES -> Icons.Default.Image
                    MediaFilter.VIDEOS -> Icons.Default.Videocam
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (source == StatusSource.SAVED_VAULT) "No Saved Statuses" else "No Statuses Detected",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (source == StatusSource.SAVED_VAULT) {
                "Saved photos and videos in DCIM/Status Saver will appear here."
            } else {
                "Open ${source.label} and view some statuses first. Viewed statuses will cache and show up here!"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onRefresh,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.minimumInteractiveComponentSize()
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Refresh Now")
        }
    }
}

@Composable
fun FullScreenMediaViewer(
    initialItem: StatusItem,
    items: List<StatusItem>,
    imageLoader: ImageLoader,
    onDismiss: () -> Unit,
    onSave: (StatusItem) -> Unit,
    onDelete: (StatusItem, StatusItem?) -> Unit,
    onShare: (StatusItem) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isControlsVisible by remember { mutableStateOf(true) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Ensure list has at least initialItem
    val safeItems = remember(items, initialItem) {
        if (items.any { it.id == initialItem.id }) items else listOf(initialItem) + items
    }
    val initialIndex = remember(safeItems, initialItem) {
        safeItems.indexOfFirst { it.id == initialItem.id }.coerceAtLeast(0)
    }
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { safeItems.size }
    )

    val currentItem = safeItems.getOrNull(
        pagerState.currentPage.coerceIn(0, (safeItems.size - 1).coerceAtLeast(0))
    ) ?: initialItem

    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .zIndex(100f)
    ) {
        // Full-Bleed Horizontal Pager for swiping left and right through statuses
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxSize(),
            key = { safeItems.getOrNull(it)?.id ?: "item_$it" }
        ) { pageIndex ->
            val pageItem = safeItems.getOrNull(pageIndex) ?: return@HorizontalPager
            val isSelected = pagerState.currentPage == pageIndex

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (pageItem.isVideo) {
                    VideoPlayerView(
                        uri = pageItem.uri,
                        isPlaying = isSelected,
                        onTap = { isControlsVisible = !isControlsVisible },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ZoomableStatusImage(
                        uri = pageItem.uri,
                        name = pageItem.name,
                        imageLoader = imageLoader,
                        onTap = { isControlsVisible = !isControlsVisible },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Minimal Frosted Top Bar (Auto-hiding)
        AnimatedVisibility(
            visible = isControlsVisible,
            enter = fadeIn(animationSpec = tween(220)) + slideInVertically(animationSpec = tween(220)) { -it },
            exit = fadeOut(animationSpec = tween(180)) + slideOutVertically(animationSpec = tween(180)) { -it },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Surface(
                color = NavyDarkBackground.copy(alpha = 0.82f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentItem.name,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SagePrimary.copy(alpha = 0.22f),
                                border = BorderStroke(1.dp, SagePrimary.copy(alpha = 0.45f))
                            ) {
                                Text(
                                    text = "${pagerState.currentPage + 1}/${safeItems.size}",
                                    color = SageAccent,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                        }
                        val dateFormatted = remember(currentItem.dateModified) {
                            SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
                                .format(Date(currentItem.dateModified))
                        }
                        Text(
                            text = "$dateFormatted • ${formatFileSize(currentItem.size)}",
                            color = SlateTextSecondary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // Minimal Frosted Bottom Dock (Auto-hiding)
        AnimatedVisibility(
            visible = isControlsVisible,
            enter = fadeIn(animationSpec = tween(220)) + slideInVertically(animationSpec = tween(220)) { it },
            exit = fadeOut(animationSpec = tween(180)) + slideOutVertically(animationSpec = tween(180)) { it },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Surface(
                color = NavyDarkBackground.copy(alpha = 0.88f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Actions based on source
                    if (currentItem.source == StatusSource.SAVED_VAULT) {
                        // Delete Button in Saved Vault
                        Button(
                            onClick = { showDeleteConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ErrorRed.copy(alpha = 0.18f),
                                contentColor = ErrorRed
                            ),
                            border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.6f)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("viewer_delete_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(19.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Delete",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else if (!currentItem.isSaved) {
                        // Save Button
                        Button(
                            onClick = { onSave(currentItem) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF09090B)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("viewer_save_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = Color(0xFF09090B),
                                modifier = Modifier.size(19.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Save Status",
                                color = Color(0xFF09090B),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        // Saved in Vault indicator
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceHighlight,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Saved",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Single Share Button in bottom dock
                    Button(
                        onClick = { onShare(currentItem) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceHighlight,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("viewer_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Share",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Delete Confirmation AlertDialog
        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(30.dp)
                    )
                },
                title = {
                    Text(
                        text = "Delete Saved Status?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete this status? This will permanently remove the file from your saved collection and device storage.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteConfirmDialog = false
                            val currentIndex = safeItems.indexOfFirst { it.id == currentItem.id }
                            val nextItem = when {
                                safeItems.size <= 1 -> null
                                currentIndex < safeItems.size - 1 -> safeItems[currentIndex + 1]
                                currentIndex > 0 -> safeItems[currentIndex - 1]
                                else -> null
                            }
                            onDelete(currentItem, nextItem)
                            if (nextItem == null) {
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.testTag("confirm_delete_button")
                    ) {
                        Text("Delete", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteConfirmDialog = false },
                        modifier = Modifier.testTag("cancel_delete_button")
                    ) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

private fun formatTimeMs(timeMs: Long): String {
    val totalSeconds = (timeMs / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}

@Composable
fun VideoPlayerView(
    uri: Uri,
    isPlaying: Boolean,
    onTap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = ExoMediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
    }

    var isPlaybackActive by remember { mutableStateOf(isPlaying) }
    var currentPosMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var areControlsVisible by remember { mutableStateOf(true) }

    LaunchedEffect(isPlaying) {
        isPlaybackActive = isPlaying
        exoPlayer.playWhenReady = isPlaying
    }

    // Auto-hide controls after 3 seconds of active playback
    LaunchedEffect(areControlsVisible, isPlaybackActive) {
        if (areControlsVisible && isPlaybackActive) {
            delay(3000L)
            areControlsVisible = false
        }
    }

    // Continuous ticker for current playback position and duration
    LaunchedEffect(isPlaybackActive) {
        while (isPlaybackActive) {
            currentPosMs = exoPlayer.currentPosition.coerceAtLeast(0L)
            val dur = exoPlayer.duration
            if (dur > 0) {
                durationMs = dur
            }
            delay(200L)
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        areControlsVisible = !areControlsVisible
                        onTap()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Center Play / Pause Button Overlay
        AnimatedVisibility(
            visible = areControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.65f))
                    .border(1.5.dp, Color.White.copy(alpha = 0.75f), CircleShape)
                    .clickable {
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                            isPlaybackActive = false
                        } else {
                            exoPlayer.play()
                            isPlaybackActive = true
                        }
                        areControlsVisible = true
                    }
                    .testTag("video_play_pause_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaybackActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaybackActive) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        // Bottom Time Slider & Position Indicator Overlay
        AnimatedVisibility(
            visible = areControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, end = 20.dp, bottom = 104.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.70f), RoundedCornerShape(16.dp))
                    .border(0.8.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTimeMs(currentPosMs),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = formatTimeMs(durationMs),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Slider(
                        value = currentPosMs.toFloat(),
                        onValueChange = { newPos ->
                            currentPosMs = newPos.toLong()
                            exoPlayer.seekTo(newPos.toLong())
                            areControlsVisible = true
                        },
                        valueRange = 0f..(durationMs.coerceAtLeast(1L).toFloat()),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTrackColor = Color.White.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("video_time_slider")
                    )
                }
            }
        }
    }
}

@Composable
fun ZoomableStatusImage(
    uri: Uri,
    name: String,
    imageLoader: ImageLoader,
    onTap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "zoom_scale"
    )

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(1f, 4f)
        scale = newScale
        if (newScale > 1.05f) {
            val maxOffset = 500f * (newScale - 1f)
            offset = Offset(
                x = (offset.x + panChange.x).coerceIn(-maxOffset, maxOffset),
                y = (offset.y + panChange.y).coerceIn(-maxOffset, maxOffset)
            )
        } else {
            offset = Offset.Zero
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() },
                    onDoubleTap = {
                        if (scale > 1.2f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 2.5f
                        }
                    }
                )
            }
            .transformable(
                state = transformState,
                enabled = scale > 1.05f,
                lockRotationOnZoomPan = true
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(uri)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
            contentDescription = name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = animatedScale,
                    scaleY = animatedScale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    uiState: StatusUiState,
    onDismiss: () -> Unit,
    onToggleAutoSave: (Boolean) -> Unit,
    onToggleBusiness: (Boolean) -> Unit,
    onRunSyncNow: () -> Unit,
    onOpenGuide: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            Text(
                text = "Status Saver Settings",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            // WhatsApp Business Statuses Toggle Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "WhatsApp Business Statuses",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Show WhatsApp Business tab and statuses. Disable if you only use personal WhatsApp.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(
                        checked = uiState.isBusinessEnabled,
                        onCheckedChange = onToggleBusiness,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = Color.White,
                            uncheckedThumbColor = Color(0xFFA1A1AA),
                            uncheckedTrackColor = Color(0xFF27272A)
                        ),
                        modifier = Modifier.testTag("business_toggle_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Auto-Save Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Automatic Background Backup",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Periodically scans and saves new statuses to DCIM/Status Saver",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Switch(
                            checked = uiState.isAutoSaveEnabled,
                            onCheckedChange = onToggleAutoSave,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = Color.White,
                                uncheckedThumbColor = Color(0xFFA1A1AA),
                                uncheckedTrackColor = Color(0xFF27272A)
                            ),
                            modifier = Modifier.testTag("auto_save_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sync Frequency: Every 1 Hour",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        OutlinedButton(
                            onClick = onRunSyncNow,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Sync Now")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Target Storage Folder Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Save Location",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "DCIM/Status Saver (Public Gallery)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "All saved photos and videos are indexed directly into your Google Photos and Gallery app.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onOpenGuide,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Storage Access Framework Guide")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Info & Developer Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Status Saver",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Developed by: Arslan",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FolderGuideDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(44.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "How to Grant Access",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "When Android's file picker opens:\n\n" +
                            "1. Navigate to:\nAndroid > media > com.whatsapp > WhatsApp > Media\n\n" +
                            "2. Select the '.Statuses' folder.\n(Enable 'Show hidden files' in top right 3-dots if not visible)\n\n" +
                            "3. Tap 'USE THIS FOLDER' button at the bottom and confirm.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Got It")
                }
            }
        }
    }
}

fun formatFileSize(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB")
    val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(Locale.getDefault(), "%.1f %s", bytes / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}
