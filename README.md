# 🎵 Music Player App

Una moderna aplicación de reproducción de música desarrollada con **Kotlin** y **Jetpack Compose**, que ofrece una experiencia fluida, dinámica y adaptada a las mejores prácticas de arquitectura en Android.

## 🚀 Características Principales

- 🔊 **Reproducción de música en segundo plano** usando `ForegroundService`
- 🧭 **Navegación entre pantallas** con Jetpack Navigation y Deep Linking desde notificaciones
- 🛎️ **Notificaciones personalizadas** con controles integrados (Play, Pause, Next, Previous)
- 📁 **Gestión de permisos de almacenamiento** para acceder a archivos de audio locales
- 🧠 **Arquitectura Clean MVVM** con `ViewModel`, `StateFlow` y `Navigation`
- 🧠 **Persistencia del estado de la música** incluso cuando la app se reinicia desde la notificación
- 📡 **BroadcastReceiver** para actualizar la UI en tiempo real desde el servicio
- 🛠️ Integración de servicios (`Service` y `BroadcastReceiver`) y comunicación con la interfaz mediante `StateFlow`

## 🧩 Tecnologías Usadas

- Kotlin
- Jetpack Compose
- StateFlow / LiveData
- Navigation Component
- Foreground Services
- Notification Manager
- Permission Handling (runtime permissions)
- JSON Serialization (Kotlinx)
- Android Architecture Components

## 📷 Capturas de pantalla
<p align="center">
  <img src="assets/image1.png" width="30%">
  <img src="assets/image2.png" width="30%">
  <img src="assets/image3.png" width="30%">
</p>



_¡Muy pronto!_

## 🏁 Cómo correr el proyecto

1. Clona este repositorio:
   ```bash
   git clone git@github.com:tuusuario/music-player-app.git
   cd music-player-app
