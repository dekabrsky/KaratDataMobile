package com.example.karatdatamobile

import android.os.Bundle
import com.example.karatdatamobile.di.ContextModule
import com.github.terrakok.cicerone.*
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import moxy.MvpAppCompatActivity
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity: MvpAppCompatActivity() {
    private lateinit var navigator: Navigator

    @Inject
    lateinit var cicerone: Cicerone<Router>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toothpick.openRootScope()
            .installModules(ContextModule(this))
            .inject(this)

        navigator = AppNavigator(this, R.id.main_container)
        cicerone.getNavigatorHolder().setNavigator(navigator)

        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf<Command>(Replace(FragmentScreen { FlowFragment() })))
        }
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.getNavigatorHolder().removeNavigator()
    }

    override fun onBackPressed() {
        cicerone.router.exit()
    }
}