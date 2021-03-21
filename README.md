# Android Image Search App 
---
A simple image search app which uses some of the <a href="https://developer.android.com/jetpack/guide" target="_blank">Android Architecture Components</a> and bunch of
other libraries to search random images from a web service and display them in a RecyclerView.

## Description
---
The app uses <a href="https://unsplash.com/" target="_blank">Unsplash API</a> for searching free images from the website, <a href="https://square.github.io/retrofit/" target="_blank">Retrofit</a> is there to execute this network request and communicate with the web service following with Android MVVM Design Pattern to achieve more maintainable architecture. Also the app uses Single-Activity pattern.
</br></br>
The main purpose of the app to get started with the Android MVVM Architecture, communicating with web services also with some <a href="https://developer.android.com/jetpack" target="_blank">Android Jetpack</a> libraries. It can be considered as a beginner app so it is fully open for improvements, code fixes from you.

### Unsplash API Key
---
The app uses the <a href="https://unsplash.com/" target="_blank">Unsplash API</a> to load images. To use the API, you have to create an account and get a developer API key. Visit the <a href="https://unsplash.com/documentation" target="_blank">Unsplash API Documentation</a> for more information.
</br></br>
![Unsplash Website](https://github.com/ahmetbozkan/Android-Image-Search-App-MVVM/blob/master/screenshots/unsplash.PNG)

### Libraries Used
---
- <a href="https://developer.android.com/jetpack/guide" target="_blank">Architecture</a>
  - <a href="https://developer.android.com/guide/navigation?gclid=Cj0KCQiAj9iBBhCJARIsAE9qRtB8q19xWrOMU0xmUn61XdeIv8N7920hIVv1NtWswr5ZegovD3HwUYsaAm2IEALw_wcB&gclsrc=aw.ds" target="_blank">Jetpack Navigation</a> - To handle in-app navigation easily.
  - <a href="https://developer.android.com/topic/libraries/architecture/livedata" target="_blank">LiveData</a> - To build a lifecycle-aware dataset and notify the UI immediately when related data changes.
  - <a href="https://developer.android.com/jetpack/guide" target="_blank">Model-View-ViewModel</a> - To have much more maintainable and clean architecture, much less boilerplate code and execute asynchronous tasks easily.
  - <a href="https://developer.android.com/training/dependency-injection" target="_blank">Dagger</a> - For dependency injection.
  - <a href="https://developer.android.com/jetpack/androidx/releases/room?gclid=Cj0KCQiAj9iBBhCJARIsAE9qRtChvAxEUJ0dpFkx9Z0cAOCfpDjovGNB0AbJ05AA7NEdAyzKCAKT8_oaAk7QEALw_wcB&gclsrc=aw.ds" target="_blank">Room</a> - For creating a SQLite database to save desired images to display them later.
- Third party libraries
  - <a href="https://github.com/bumptech/glide" target="_blank">Glide</a> - For image loading.

### Libraries to be Added
---
- <a href="https://developer.android.com/topic/libraries/architecture/paging/v3-overview" target="_blank">Paging 3</a> - To handle pagination and load large datasets -images in my case- more efficiently.

### Screenshots
--- 
![Home Screen](https://github.com/ahmetbozkan/Android-Image-Search-App-MVVM/blob/master/screenshots/home_ss.PNG)    ![Search Screen](https://github.com/ahmetbozkan/Android-Image-Search-App-MVVM/blob/master/screenshots/search_ss.PNG) ![Details Screen](https://github.com/ahmetbozkan/Android-Image-Search-App-MVVM/blob/master/screenshots/details_ss.PNG)


### Licence
---
- Copyright 2021 © <a href="https://github.com/ahmetbozkan" target="_blank">Ahmet Bozkan</a>.
