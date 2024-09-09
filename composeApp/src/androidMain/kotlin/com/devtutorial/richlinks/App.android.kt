package com.devtutorial.richlinks

import android.content.Intent
import android.net.Uri

actual fun openLink(link: String) {
    val context = ContextProvider.getContext()
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}