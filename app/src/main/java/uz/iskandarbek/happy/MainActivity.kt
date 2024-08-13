package uz.iskandarbek.happy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.widget.ImageView
import uz.iskandarbek.happy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Barmoq izi mavjud va autentifikatsiya qilish mumkin
                setupBiometricPrompt()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // Barmoq izi skaneri mavjud emas
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // Barmoq izi skaneri vaqtincha mavjud emas
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Foydalanuvchi barmoq izlarini o'rnatmagan
            }
        }
    }

    private fun setupBiometricPrompt() {
        val biometricPrompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Xato haqida xabar berish
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Autentifikatsiya muvaffaqiyatli
                    // Keyingi faoliyatni bajarish, masalan, yangi ekranga o'tish
                    // Keyingi Activity'ga o'tish
                    val intent = Intent(this@MainActivity, NextActivity::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Autentifikatsiya muvaffaqiyatsiz
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Barmoq izi autentifikatsiyasi")
            .setSubtitle("Barmoq izingizni qo'llang")
            .setNegativeButtonText("Bekor qilish")
            .build()

        val fingerprintIcon: ImageView = findViewById(R.id.fingerprint_icon)
        fingerprintIcon.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}