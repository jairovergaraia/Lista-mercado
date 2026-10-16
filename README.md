# Lista de Mercado (Android, Kotlin + Jetpack Compose)

Misma app que la versión de iPhone: catálogo por grupos (Alimentos, Aseo del hogar,
Cuidado personal), precios, cantidades y total del mercado. Funciona en Android 8.0 o superior.

## Pantallas de cualquier tamaño

La interfaz se adapta sola: en teléfono vertical muestra una pantalla a la vez con barra
inferior; en tableta o teléfono horizontal (desde 720 dp de ancho) muestra Productos y
Mi lista lado a lado. Las tarjetas del catálogo se reparten en tantas columnas como quepan
y los textos respetan el tamaño de letra del sistema.

## Generar el APK sin Android Studio (GitHub Actions)

1. Crea un repositorio nuevo en github.com y sube el contenido de esta carpeta
   (arrastrando los archivos en "Add file > Upload files"; incluye la carpeta `.github`).
2. Entra a la pestaña **Actions**, abre "Compilar APK" y, si no arrancó solo, pulsa **Run workflow**.
3. Cuando termine (unos 5 minutos), abre la ejecución y descarga **ListaMercado-apk**
   (un .zip que contiene `app-debug.apk`).
4. Pasa el APK al teléfono e instálalo (ver "Instalar en tu teléfono").

## Generar el APK con Android Studio

Requisitos: Windows, Mac o Linux con **Android Studio** (gratis) y conexión a internet
la primera vez (descarga Gradle y las librerías).

1. Abre Android Studio > **Open** y elige la carpeta `ListaMercadoAndroid`.
2. Espera a que termine "Gradle sync" (varios minutos la primera vez). Si pide actualizar
   algún plugin o el JDK, acepta los cambios sugeridos.
3. Menú **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Al terminar, pulsa **locate** en el aviso. El archivo queda en
   `app/build/outputs/apk/debug/app-debug.apk`.

### Instalar en tu teléfono

**Opción A, con cable (la más simple):**
1. En el teléfono: Ajustes > Acerca del teléfono > toca 7 veces "Número de compilación".
2. Ajustes > Opciones de desarrollador > activa **Depuración USB**.
3. Conecta el cable, acepta "Permitir depuración USB" y en Android Studio pulsa **Run** (▶).

**Opción B, enviando el APK:**
1. Pasa `app-debug.apk` al teléfono (WhatsApp, correo, Drive o cable).
2. Ábrelo desde el teléfono y permite "Instalar apps desconocidas" para esa aplicación cuando lo pida.
3. Pulsa Instalar.

## Cómo se usa

- **Productos:** elige la sección, toca un producto para añadirlo a la lista (toca de nuevo para quitarlo).
  Mantén pulsado un producto para eliminarlo del catálogo. El botón **+** crea productos nuevos.
- **Mi lista:** escribe el precio, ajusta la cantidad con − y +, marca lo comprado,
  y quita productos con la X. La barra verde muestra el **total del mercado**.
- Los datos se guardan en el teléfono.

## Publicar en Google Play (opcional)

Se necesita una cuenta de Google Play Console (pago único de 25 USD) y generar un
**Android App Bundle firmado** desde Build > Generate Signed Bundle / APK.

## Personalizar

- Precios y productos de ejemplo: `Models.kt` (`catalogoInicial`).
- Moneda: función `pesos()` en `Models.kt` (hoy en pesos colombianos).
- Colores: `Seccion` en `Models.kt` y `MainActivity.kt`.
