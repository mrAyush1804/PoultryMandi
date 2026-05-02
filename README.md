# PoultryMandi 🐔

PoultryMandi is a professional, real-time poultry market rate tracking platform built for farmers, traders, and retailers. The app provides instant access to live Mandi prices, company-wise historical trends, and official rate card posters (Paper Rates) with a highly intuitive, farmer-friendly interface.

## 🚀 Features

- **Live Market Rates**: Real-time tracking of Broiler, Eggs, and Chick prices across multiple states and cities (MP, Maharashtra, CG, Gujarat, etc.).
- **Dynamic Island Tracker**: A specialized UI component that expands to show a detailed chronological feed of price updates, 3-day averages, and intra-day volatility.
- **Paper Rate Feed**: A beautiful, scrollable gallery of official rate card posters published by trader associations (e.g., PBTA), integrated with **Supabase**.
- **Real-Time Alerts**: Instant "Rate Updated" banners and push notifications (FCM) when prices change in your selected city.
- **Farmer-Friendly UX**: Large readable fonts, multi-language support (English, Hindi, Marathi), and a simplified single-page layout.
- **Smart Authentication**: Secure login via Google Sign-In and Email Link, followed by a personalized "Complete Profile" flow for better market insights.
- **Professional Analytics**: Multi-company comparison tables with zebra-striping, trend indicators (▲▼), and monospace price alignment.

## 🏗️ Architecture

The project follows **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern for a scalable and testable codebase.

- **Presentation Layer**: Built entirely with **Jetpack Compose**, using a state-driven approach with `StateFlow`.
- **Domain Layer**: Contains pure Kotlin data models and repository interfaces, ensuring business logic is decoupled from external frameworks.
- **Data Layer**: Implements the repository interfaces, managing data from **Firebase Firestore**, **Supabase**, and local **SharedPreferences**.

### Project Structure
```text
com.example.poultrymandi.app/
├── Core/               # Common UI components, Theme, Navigation, Network, FCM
├── di/                 # Hilt Modules for Dependency Injection
└── feature/            # Feature-based modules
    ├── home/           # Live rates, State/City filtering, Dynamic Island
    ├── PaperRate/      # Supabase-integrated image feed
    ├── auth/           # Login, Signup, Google Auth
    ├── notification/   # Price alerts and system messages
    ├── profile/        # User settings and profile management
    └── CompleteProfile/# Specialized on-boarding flow
```

## 🛠️ Tech Stack

- **Language**: 100% Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Dependency Injection**: Hilt (Dagger)
- **Backend**: 
  - **Firebase**: Firestore (Real-time DB), Auth, Messaging (FCM), Analytics.
  - **Supabase**: Postgrest (API), Storage (Image Hosting).
- **Navigation**: Type-safe Navigation Compose with Kotlin Serialization.
- **Image Loading**: Coil3
- **Local Storage**: Room & SharedPreferences.
- **Networking**: Ktor (for Supabase integration).

## 📊 Firestore Data Structure
- `rates/{date}/states/{stateId}/cities/{cityId}`: Handles real-time market price summaries.
- `companyRates/{date}/{cityId}/details/updates`: Powers the historical feed in the Dynamic Island.

## 📦 Getting Started

1. Clone the repository: `git clone https://github.com/your-repo/poultry-mandi.git`
2. Add your `google-services.json` to the `app/` directory.
3. Ensure Supabase credentials are configured in `feature/paperrate/di/PaperRateModule.kt`.
4. Build and run the app on Android Studio Ladybug or higher.

## 📄 License
This project is confidential and proprietary. All rights reserved by **Ninjafarm Services Pvt. Ltd.**
