package com.skcodes.runntrak

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.DialogHost
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.skcodes.presentation.designsystem.RunNTrakTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration


class MainActivity : ComponentActivity() {

    private lateinit var splitInstallManager :SplitInstallManager
    val splitInstallListener = SplitInstallStateUpdatedListener{ state ->
        when(state.status()){
            SplitInstallSessionStatus.INSTALLED ->{
                viewModel.toggleAnalyticsInstallDialog(false)
                Toast.makeText(this,
                    R.string.install_successful,
                    Toast.LENGTH_LONG).show()
            }

            SplitInstallSessionStatus.INSTALLING ->{
                viewModel.toggleAnalyticsInstallDialog(true)
            }

            SplitInstallSessionStatus.DOWNLOADING ->{
                viewModel.toggleAnalyticsInstallDialog(true)
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION ->{
                splitInstallManager.startConfirmationDialogForResult(state,this,0)
            }
            SplitInstallSessionStatus.FAILED ->{
                viewModel.toggleAnalyticsInstallDialog(false)
                Toast.makeText(this,
                    R.string.install_failed,
                    Toast.LENGTH_LONG).show()
            }
        }

    }

    val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                viewModel.state.isCheckingAuth
            }
        }

        splitInstallManager = SplitInstallManagerFactory.create(this)
        setContent {
            RunNTrakTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    if(!viewModel.state.isCheckingAuth){
                        val navHostController = rememberNavController()
                        NavigationRoot(navHostController = navHostController,
                            isLoggedIn = viewModel.state.isLoggedIn,
                            onAnalyticsClick ={installOrStartAnalyticsFeature()}
                        )


                    }

                    if(viewModel.state.showAnalyticsInstallDialog){
                        Dialog(
                            onDismissRequest = {}
                        ){
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(color = MaterialTheme.colorScheme.surface)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = getString(R.string.installing_module))
                            }
                        }
                    }

                }

            }
        }
    }

    private fun installOrStartAnalyticsFeature() {
        if(splitInstallManager.installedModules.contains("analytics_feature")){
            Intent().setClassName(packageName,"com.skcodes.analytics.analytics_feature.AnalyticsActivity")
                .also ( ::startActivity)
            return
        }

        val splitInstallRequest = SplitInstallRequest.newBuilder()
            .addModule("analytics_feature")
            .build()

        splitInstallManager
            .startInstall(splitInstallRequest)
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(this,R.string.cound_not_install_analytics,Toast.LENGTH_LONG).show()
            }
    }

    override fun onResume() {
        super.onResume()
        splitInstallManager.registerListener(splitInstallListener)
    }

    override fun onPause() {
        super.onPause()
        splitInstallManager.unregisterListener(splitInstallListener)
    }
}
