package com.listamercado.app

import androidx.compose.ui.graphics.Color
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

enum class Seccion(val titulo: String, val color: Color) {
    ALIMENTOS("Alimentos", Color(0xFF5BB373)),
    ASEO_HOGAR("Aseo del hogar", Color(0xFF4DA6D9)),
    CUIDADO_PERSONAL("Cuidado personal", Color(0xFFEE859D))
}

enum class Grupo(val titulo: String, val seccion: Seccion) {
    // Alimentos
    FRUTAS_VERDURAS("Frutas y verduras", Seccion.ALIMENTOS),
    CARNES("Carnes y huevos", Seccion.ALIMENTOS),
    LACTEOS("Lácteos", Seccion.ALIMENTOS),
    GRANOS("Granos y cereales", Seccion.ALIMENTOS),
    PANADERIA("Panadería", Seccion.ALIMENTOS),
    BEBIDAS("Bebidas", Seccion.ALIMENTOS),
    DESPENSA("Despensa", Seccion.ALIMENTOS),

    // Aseo del hogar
    LIMPIEZA("Limpieza general", Seccion.ASEO_HOGAR),
    COCINA("Cocina", Seccion.ASEO_HOGAR),
    LAVANDERIA("Lavandería", Seccion.ASEO_HOGAR),
    DESECHABLES("Papel y desechables", Seccion.ASEO_HOGAR),

    // Cuidado personal
    HIGIENE("Higiene", Seccion.CUIDADO_PERSONAL),
    CABELLO("Cabello", Seccion.CUIDADO_PERSONAL),
    PIEL("Piel y cuidado", Seccion.CUIDADO_PERSONAL)
}

data class Producto(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val emoji: String,
    val grupo: Grupo,
    val precio: Double = 0.0,
    val cantidad: Int = 1,
    val enLista: Boolean = false,
    val comprado: Boolean = false
) {
    val subtotal: Double get() = precio * cantidad

    companion object {
        private fun p(nombre: String, emoji: String, grupo: Grupo, precio: Double) =
            Producto(nombre = nombre, emoji = emoji, grupo = grupo, precio = precio)

        /** Catálogo inicial. Los precios son de ejemplo: edítalos desde la lista. */
        val catalogoInicial: List<Producto> = listOf(
            // Frutas y verduras
            p("Plátano", "🍌", Grupo.FRUTAS_VERDURAS, 3500.0),
            p("Manzana", "🍎", Grupo.FRUTAS_VERDURAS, 1800.0),
            p("Tomate", "🍅", Grupo.FRUTAS_VERDURAS, 4000.0),
            p("Cebolla", "🧅", Grupo.FRUTAS_VERDURAS, 3000.0),
            p("Zanahoria", "🥕", Grupo.FRUTAS_VERDURAS, 2500.0),
            p("Aguacate", "🥑", Grupo.FRUTAS_VERDURAS, 3500.0),
            p("Papa", "🥔", Grupo.FRUTAS_VERDURAS, 3200.0),
            // Carnes y huevos
            p("Pollo", "🍗", Grupo.CARNES, 14000.0),
            p("Carne de res", "🥩", Grupo.CARNES, 22000.0),
            p("Pescado", "🐟", Grupo.CARNES, 18000.0),
            p("Huevos", "🥚", Grupo.CARNES, 15000.0),
            // Lácteos
            p("Leche", "🥛", Grupo.LACTEOS, 4500.0),
            p("Queso", "🧀", Grupo.LACTEOS, 9000.0),
            p("Yogur", "🍦", Grupo.LACTEOS, 6000.0),
            p("Mantequilla", "🧈", Grupo.LACTEOS, 7500.0),
            // Granos y cereales
            p("Arroz", "🍚", Grupo.GRANOS, 5000.0),
            p("Frijoles", "🫘", Grupo.GRANOS, 7000.0),
            p("Pasta", "🍝", Grupo.GRANOS, 3500.0),
            p("Avena", "🥣", Grupo.GRANOS, 5500.0),
            // Panadería
            p("Pan", "🍞", Grupo.PANADERIA, 6000.0),
            p("Galletas", "🍪", Grupo.PANADERIA, 3000.0),
            p("Arepas", "🫓", Grupo.PANADERIA, 4500.0),
            // Bebidas
            p("Jugo", "🧃", Grupo.BEBIDAS, 5000.0),
            p("Agua", "💧", Grupo.BEBIDAS, 3500.0),
            p("Café", "☕", Grupo.BEBIDAS, 12000.0),
            p("Gaseosa", "🥤", Grupo.BEBIDAS, 6500.0),
            // Despensa
            p("Aceite", "🫒", Grupo.DESPENSA, 14000.0),
            p("Azúcar", "🍬", Grupo.DESPENSA, 4500.0),
            p("Sal", "🧂", Grupo.DESPENSA, 1800.0),
            // Limpieza general
            p("Desinfectante", "🧴", Grupo.LIMPIEZA, 8000.0),
            p("Límpiapisos", "🧹", Grupo.LIMPIEZA, 7500.0),
            p("Cloro", "🫧", Grupo.LIMPIEZA, 4500.0),
            p("Bolsas de basura", "🗑️", Grupo.LIMPIEZA, 5000.0),
            // Cocina
            p("Lavaplatos", "🍽️", Grupo.COCINA, 6000.0),
            p("Esponjas", "🧽", Grupo.COCINA, 3500.0),
            // Lavandería
            p("Detergente", "🧺", Grupo.LAVANDERIA, 16000.0),
            p("Suavizante", "🌸", Grupo.LAVANDERIA, 9000.0),
            // Papel y desechables
            p("Papel higiénico", "🧻", Grupo.DESECHABLES, 18000.0),
            p("Servilletas", "📄", Grupo.DESECHABLES, 3000.0),
            // Higiene
            p("Jabón de baño", "🧼", Grupo.HIGIENE, 6000.0),
            p("Crema dental", "🪥", Grupo.HIGIENE, 7000.0),
            p("Desodorante", "🧴", Grupo.HIGIENE, 11000.0),
            p("Toallas higiénicas", "🌷", Grupo.HIGIENE, 8000.0),
            // Cabello
            p("Champú", "🚿", Grupo.CABELLO, 15000.0),
            p("Acondicionador", "💆", Grupo.CABELLO, 15000.0),
            // Piel y cuidado
            p("Crema corporal", "🧴", Grupo.PIEL, 14000.0),
            p("Protector solar", "☀️", Grupo.PIEL, 32000.0),
            p("Afeitadoras", "🪒", Grupo.PIEL, 9000.0)
        )
    }
}

/** Formato en pesos colombianos, sin decimales. */
fun Double.pesos(): String {
    val f = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
    f.maximumFractionDigits = 0
    f.minimumFractionDigits = 0
    return f.format(this)
}
