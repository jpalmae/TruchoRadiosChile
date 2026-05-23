# 📻 Trucho Radios Chile

App Android para escuchar radios chilenas en vivo.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-orange)
![Min SDK](https://img.shields.io/badge/Min%20SDK-24-green)

## ✨ Características

- 📻 **271 radios chilenas** con URLs de streaming verificadas
- 📍 **16 regiones** oficiales de Chile (Arica y Parinacota → Magallanes)
- 🎵 **30 géneros** musicales
- 🎧 Reproducción en segundo plano con notificación
- 🔍 Búsqueda por nombre
- ❤️ Sistema de favoritos
- 🎨 Tema oscuro Material 3
- 📱 Mini player flotante + Bottom navigation
- 🚀 Auto-play al seleccionar emisora

## 🛠 Stack Tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + Clean Architecture |
| Reproductor | ExoPlayer (Media3) |
| Audio Background | MediaSessionService |
| DB Local | Room |
| Inyección | Hilt |
| Imágenes | Coil |

## 📱 Screenshots

*Coming soon*

## 🔧 Compilar

```bash
# Clonar
git clone https://github.com/jpalmae/TruchoRadiosChile.git
cd TruchoRadiosChile

# Compilar debug APK
./gradlew assembleDebug

# El APK se genera en:
# app/build/outputs/apk/debug/app-debug.apk
```

## 📊 Datos

Las radios se obtienen de [radio-browser.info](https://www.radio-browser.info/) — base de datos abierta de radios por streaming. Las 271 radios incluyen emisoras de todas las regiones de Chile.

## 📄 Licencia

MIT
