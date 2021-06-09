package com.example.karatdatamobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.githublist.di.ContextModule
import com.example.karatdatamobile.main.SectionsPagerAdapter
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.tabs.TabLayout
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity: AppCompatActivity() {
    private lateinit var navigator: Navigator

    @Inject
    lateinit var cicerone: Cicerone<Router>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)


        Toothpick.openRootScope()
            .installModules(ContextModule(this))
            .inject(this)

        navigator = AppNavigator(this, R.id.MainContainer)


        cicerone.getNavigatorHolder().setNavigator(navigator)

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