package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.data.model.StatusSource
import com.example.ui.screens.StatusMainScreen
import com.example.ui.theme.DualStatusSaverTheme
import com.example.ui.viewmodel.StatusViewModel

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel: StatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle navigation if opened from notification
        handleIncomingIntent(intent)

        setContent {
            DualStatusSaverTheme {
                // Storage Access Framework Launcher for WhatsApp
                val whatsAppLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocumentTree()
                ) { uri: Uri? ->
                    if (uri != null) {
                        try {
                            contentResolver.takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            Log.i(TAG, "Persisted read URI permission for WhatsApp: $uri")
                            viewModel.onFolderPermissionGranted(StatusSource.WHATSAPP, uri)
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to take persistable URI permission for WhatsApp", e)
                        }
                    }
                }

                // Storage Access Framework Launcher for WhatsApp Business
                val businessLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocumentTree()
                ) { uri: Uri? ->
                    if (uri != null) {
                        try {
                            contentResolver.takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            Log.i(TAG, "Persisted read URI permission for WA Business: $uri")
                            viewModel.onFolderPermissionGranted(StatusSource.BUSINESS, uri)
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to take persistable URI permission for WA Business", e)
                        }
                    }
                }

                // Notification Permission Launcher (Android 13+)
                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    Log.d(TAG, "Notification permission result: $isGranted")
                }



                Surface(modifier = Modifier.fillMaxSize()) {
                    StatusMainScreen(
                        viewModel = viewModel,
                        onRequestWhatsAppPermission = { whatsAppLauncher.launch(null) },
                        onRequestBusinessPermission = { businessLauncher.launch(null) },
                        onRequestNotificationPermission = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        val targetTab = intent?.getStringExtra("OPEN_TAB")
        if (targetTab == "VAULT") {
            viewModel.selectTab(StatusSource.SAVED_VAULT)
        }
    }
}
