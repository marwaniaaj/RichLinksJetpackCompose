package com.devtutorial.richlinks

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openLink(link: String) {
    runCatching {
        val nsUrl = NSURL.URLWithString(link)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }.getOrElse {
        it.printStackTrace()
    }
}