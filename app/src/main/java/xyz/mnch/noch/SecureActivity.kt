package xyz.mnch.noch

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import xyz.mnch.noch.R
import java.util.concurrent.Executor

open class SecureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val executor = ContextCompat.getMainExecutor(this)

        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(executor)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.d(
                    "noch",
                    getString(R.string.error_msg_no_biometric_hardware),
                )
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.d(
                    "noch",
                    getString(R.string.error_msg_biometric_hw_unavailable),
                )
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.d(
                    "noch",
                    getString(R.string.error_msg_biometric_not_setup),
                )
        }
    }

    private fun authUser(executor: Executor) {
        // 1
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            // 2
            .setTitle(getString(R.string.auth_title))
            // 3
//            .setSubtitle(getString(R.string.auth_subtitle))
            // 4
            .setDescription(getString(R.string.auth_description))
            // 5
            .setDeviceCredentialAllowed(true)
            // 6
            .build()

        // 1
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                // 2
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("noch", "View.VISIBLE")
                }
                // 3
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d("noch", getString(R.string.error_msg_auth_error) + errString)
                    authUser(executor)
                }
                // 4
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d("noch", getString(R.string.error_msg_auth_failed))
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}
