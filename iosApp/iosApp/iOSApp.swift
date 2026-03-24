import SwiftUI

@main
struct iOSApp: App {
    @State private var showIntro = true

    var body: some Scene {
        WindowGroup {
            if showIntro {
                IntroComposeView(onComplete: {
                    showIntro = false
                })
                .ignoresSafeArea()
            } else {
                ContentView()
            }
        }
    }
}
