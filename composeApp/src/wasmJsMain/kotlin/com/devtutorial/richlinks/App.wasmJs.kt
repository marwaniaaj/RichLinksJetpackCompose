package com.devtutorial.richlinks

import kotlinx.browser.window

actual fun openLink(link: String) {
    window.open(link)
}