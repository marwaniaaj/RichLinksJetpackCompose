package com.devtutorial.richlinks

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.startup.Initializer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            App()
        }
    }
}

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ContextProvider.setContext(context.applicationContext)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

object ContextProvider {
    private lateinit var appContext: Context

    fun setContext(context: Context) {
        appContext = context
    }

    fun getContext(): Context {
        return appContext
    }
}