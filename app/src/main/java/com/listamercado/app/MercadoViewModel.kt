package com.listamercado.app

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import org.json.JSONArray
import org.json.JSONObject

class MercadoViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("lista_mercado", Context.MODE_PRIVATE)

    var productos by mutableStateOf(cargar())
        private set

    // ---- Consultas ----

    val enLista: List<Producto> get() = productos.filter { it.enLista }

    val total: Double get() = enLista.sumOf { it.subtotal }

    val totalComprado: Double get() = enLista.filter { it.comprado }.sumOf { it.subtotal }

    // ---- Acciones ----

    fun modificar(id: String, cambio: (Producto) -> Producto) {
        actualizar(productos.map { if (it.id == id) cambio(it) else it })
    }

    fun alternarEnLista(id: String) {
        modificar(id) {
            if (it.enLista) it.copy(enLista = false, comprado = false, cantidad = 1)
            else it.copy(enLista = true)
        }
    }

    fun quitarDeLista(id: String) {
        modificar(id) { it.copy(enLista = false, comprado = false, cantidad = 1) }
    }

    fun agregar(producto: Producto) {
        actualizar(productos + producto)
    }

    fun eliminarDelCatalogo(id: String) {
        actualizar(productos.filterNot { it.id == id })
    }

    fun vaciarLista() {
        actualizar(productos.map {
            if (it.enLista) it.copy(enLista = false, comprado = false, cantidad = 1) else it
        })
    }

    fun desmarcarComprados() {
        actualizar(productos.map { it.copy(comprado = false) })
    }

    // ---- Persistencia ----

    private fun actualizar(nueva: List<Producto>) {
        productos = nueva
        guardar()
    }

    private fun guardar() {
        val arr = JSONArray()
        productos.forEach { p ->
            arr.put(
                JSONObject().apply {
                    put("id", p.id)
                    put("nombre", p.nombre)
                    put("emoji", p.emoji)
                    put("grupo", p.grupo.name)
                    put("precio", p.precio)
                    put("cantidad", p.cantidad)
                    put("enLista", p.enLista)
                    put("comprado", p.comprado)
                }
            )
        }
        prefs.edit().putString("productos", arr.toString()).apply()
    }

    private fun cargar(): List<Producto> {
        val json = prefs.getString("productos", null) ?: return Producto.catalogoInicial
        return runCatching {
            val arr = JSONArray(json)
            List(arr.length()) { i ->
                val o = arr.getJSONObject(i)
                Producto(
                    id = o.getString("id"),
                    nombre = o.getString("nombre"),
                    emoji = o.getString("emoji"),
                    grupo = Grupo.valueOf(o.getString("grupo")),
                    precio = o.getDouble("precio"),
                    cantidad = o.getInt("cantidad"),
                    enLista = o.getBoolean("enLista"),
                    comprado = o.getBoolean("comprado")
                )
            }
        }.getOrDefault(Producto.catalogoInicial)
    }
}
