package com.example.gpstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.gpstracker.databinding.ActivityMainBinding
import com.example.gpstracker.di.ViewModelFactory
import com.google.firebase.FirebaseApp
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeNavigation()

        FirebaseApp.initializeApp(this)
    }

    private fun initializeNavigation() {
        val navController = findNavController()

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.signInFragment
            )
        )

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateToolbar(destination.id, appBarConfiguration)
        }
    }

    private fun updateToolbar(destinationId: Int, appBarConfiguration: AppBarConfiguration) {
        if (appBarConfiguration.topLevelDestinations.none { it == destinationId }) {
            binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        }
    }

    private fun findNavController(): NavController {
        val host = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        return host.navController
    }
}