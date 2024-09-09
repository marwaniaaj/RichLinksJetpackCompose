
# RichLinksJetpackCompose

**RichLinksJetpackCompose** is a Kotlin Compose Multiplatform app designed to render rich links, allowing users to embed and display detailed link previews seamlessly across multiple platforms. This app targets **Android**, **iOS**, **Desktop**, and **WasmJS** (WebAssembly with JavaScript), ensuring consistent user experiences across mobile, desktop, and web environments.

<img src="https://github.com/percy-g2/RichLinksJetpackCompose/blob/main/screenshot/Presentation.png" alt="Presentation"/>

## Features

- **Multiplatform**: Runs on Android, iOS, Desktop (macOS, Windows, Linux), and Web (WasmJS).
- **Rich Link Previews**: Automatically fetches and displays metadata for embedded links, including titles, descriptions, and images.
- **Composable Architecture**: Built with Jetpack Compose for a declarative, reactive UI, ensuring high-performance rendering and ease of development.
- **Responsive UI**: Optimized for various screen sizes and input methods (touch and mouse).

## Platforms Supported

| Platform | Status | Notes                              |
|----------|--------|------------------------------------|
| Android  | ✅      | Full support for all Android versions running Jetpack Compose. |
| iOS      | ✅      | Supports iOS devices using Compose Multiplatform. |
| Desktop  | ✅      | Runs on macOS, Windows, and Linux. |
| WasmJS   | 🚧      | In development. Will support modern web browsers using WebAssembly. |

## Getting Started

### Prerequisites

- **Kotlin Multiplatform**: Ensure you have Kotlin Multiplatform set up in your environment.
- **Android Studio**: Required for Android development.
- **Xcode**: Required for iOS development.
- **JDK 11+**: For building and running the desktop app.
- **Node.js**: For WasmJS/Web builds.

### Cloning the Repository

```bash
git clone https://github.com/percy-g2/RichLinksJetpackCompose.git
cd RichLinksJetpackCompose
```

### Building the Project

#### Android
1. Open the project in Android Studio.
2. Run the app on an Android emulator or physical device.

#### iOS
1. Open the `iosApp` project in Xcode.
2. Build and run the app on an iOS simulator or physical device.

#### Desktop
1. Use the command line or IntelliJ IDEA to run the desktop application:
   ```bash
   ./gradlew :desktop:run
   ```

#### WasmJS (Web)
1. Build the project using the following command:
   ```bash
   ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
   ```
2. Open the generated `index.html` in a modern web browser.

## Usage

- Embed links into the app's UI, and the app will automatically fetch and display metadata such as titles, images, and descriptions from the provided URL.
- Customize the UI using Composable functions to fit your design preferences across platforms.

## Contributing

Contributions are welcome! Please open issues or submit pull requests for any bugs, improvements, or new features.

## License

This project is licensed under the GNU General Public License (GPL). See the [LICENSE](LICENSE) file for details.
