package com.afauzi.pokedex.presentation.view.main.activity

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.afauzi.pokedex.R
import com.afauzi.pokedex.databinding.ActivityMainBinding


/**
 * Kelas utama aplikasi, bertanggung jawab untuk menampilkan navigasi dan pengaturan antarmuka pengguna.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    /**
     * Dipanggil saat aktivitas dibuat. Menginisialisasi antarmuka pengguna dan mengatur navigasi.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
    }

    /**
     * Mengatur navigasi menggunakan NavController dan bilah navigasi bawah.
     */
    private fun setUpNavigation() {
        // Temukan NavHostFragment dan dapatkan NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Mengatur navigasi bawah dengan NavController
        val bottomNavigation = binding.bottomNav
        setupWithNavController(bottomNavigation, navController)
    }
}

