package com.devtutorial.richlinks

import java.awt.Desktop
import java.net.URI

actual fun openLink(link: String) {
    Desktop.getDesktop().browse(URI(link));
}