# DeFacto Movie App

A sample Android application built with Kotlin, Jetpack libraries, and modern architecture. The app allows users to search for movies, view details, manage favorites, and keep track of search history.

## Features

- Movie search with pagination
- Movie detail view
- Add/remove movies to favorites
- View and manage favorite movie lists
- Search history management
- Sorting and filtering options
- Clean MVVM architecture with Hilt DI
- Room database for local storage
- Unit tests for ViewModels

## Tech Stack

- Kotlin
- Jetpack Compose (if used)
- Android Architecture Components (ViewModel, LiveData/StateFlow)
- Room Database
- Paging 3
- Hilt for Dependency Injection
- Mockito & JUnit for testing

## Getting Started

## Project Structure
data/ - Data sources, Room DAO, models
domain/ - Business logic, use cases
presentation/ - UI, ViewModels, events, states, effects
app/src/test/ - Unit tests

### Prerequisites

- Android Studio (Giraffe or newer recommended)
- JDK 17+
- Gradle

### Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/ahmetbabacaan/defacto-movie-app.git
   cd defacto-movie-app