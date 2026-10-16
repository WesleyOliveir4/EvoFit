//
//  evofitApp.swift
//  evofit
//
//  Created by Wesley Lopes de Oliveira on 09/10/26.
//

import SwiftUI
import FirebaseCore

@main
struct evofitApp: App {
    init() {
        FirebaseApp.configure()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
