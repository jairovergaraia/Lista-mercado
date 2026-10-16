package com.listamercado.app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import java.text.BreakIterator

// ------------------------------------------------------------
// Raíz: barra inferior con "Productos" y "Mi lista"
// ------------------------------------------------------------

/**
 * Raíz autoajustable:
 * - Pantalla angosta (teléfono vertical): una pantalla a la vez con barra inferior.
 * - Pantalla ancha (tableta o teléfono horizontal, desde 720 dp): Productos y Mi lista
 *   se muestran lado a lado.
 */
@Composable
fun AppRoot(vm: MercadoViewModel) {
    BoxWithConstraints(Modifier.fillMaxSize().background(FondoSuave)) {
        if (maxWidth >= 720.dp) PanelDoble(vm) else PantallaSimple(vm)
    }
}

@Composable
private fun PanelDoble(vm: MercadoViewModel) {
    Row(Modifier.fillMaxSize().navigationBarsPadding().imePadding()) {
        Box(Modifier.weight(1.15f).fillMaxHeight()) { CatalogoScreen(vm) }
        VerticalDivider(color = Color(0xFFE1E6DC))
        Box(Modifier.weight(1f).fillMaxHeight()) { ListaScreen(vm) }
    }
}

@Composable
private fun PantallaSimple(vm: MercadoViewModel) {
    var tab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.imePadding(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = FondoSuave,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("Productos") }
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = {
                        BadgedBox(badge = {
                            if (vm.enLista.isNotEmpty()) Badge { Text("${vm.enLista.size}") }
                        }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        }
                    },
                    label = { Text("Mi lista") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (tab == 0) CatalogoScreen(vm) else ListaScreen(vm)
        }
    }
}

// ------------------------------------------------------------
// Pantalla 1: catálogo de productos por sección y grupo
// ------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CatalogoScreen(vm: MercadoViewModel) {
    var seccion by rememberSaveable { mutableStateOf(Seccion.ALIMENTOS) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var mostrarNuevo by remember { mutableStateOf(false) }
    var aEliminar by remember { mutableStateOf<Producto?>(null) }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Text(
                "Mercado",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
            )
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar producto") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            )
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Seccion.entries.forEachIndexed { i, s ->
                    SegmentedButton(
                        selected = seccion == s,
                        onClick = { seccion = s },
                        shape = SegmentedButtonDefaults.itemShape(i, Seccion.entries.size),
                        icon = {}
                    ) {
                        Text(s.titulo, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Grupo.entries.filter { it.seccion == seccion }.forEach { grupo ->
                    val lista = vm.productos.filter {
                        it.grupo == grupo &&
                            (busqueda.isBlank() || it.nombre.contains(busqueda, ignoreCase = true))
                    }
                    if (lista.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }, key = "g_${grupo.name}") {
                            Text(
                                grupo.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = seccion.color,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                        items(lista, key = { it.id }) { p ->
                            TarjetaProducto(
                                p = p,
                                color = seccion.color,
                                onClick = { vm.alternarEnLista(p.id) },
                                onLongClick = { aEliminar = p }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { mostrarNuevo = true },
            containerColor = VerdeApp,
            contentColor = Color.White,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo producto")
        }
    }

    if (mostrarNuevo) {
        NuevoProductoDialog(
            seccionInicial = seccion,
            onCerrar = { mostrarNuevo = false },
            onGuardar = { vm.agregar(it); mostrarNuevo = false }
        )
    }

    aEliminar?.let { p ->
        AlertDialog(
            onDismissRequest = { aEliminar = null },
            title = { Text("Eliminar del catálogo") },
            text = { Text("¿Eliminar \"${p.nombre}\" del catálogo?") },
            confirmButton = {
                TextButton(onClick = { vm.eliminarDelCatalogo(p.id); aEliminar = null }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { aEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun EmojiCircle(emoji: String, color: Color, size: Dp, fontSize: TextUnit) {
    Box(
        modifier = Modifier.size(size).clip(CircleShape).background(color.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = fontSize)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TarjetaProducto(p: Producto, color: Color, onClick: () -> Unit, onLongClick: () -> Unit) {
    val forma = RoundedCornerShape(18.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color.White)
            .border(
                width = if (p.enLista) 2.5.dp else 0.5.dp,
                color = if (p.enLista) color else Color(0xFFE1E6DC),
                shape = forma
            )
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(vertical = 10.dp, horizontal = 6.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            EmojiCircle(p.emoji, color, 64.dp, 34.sp)
            Text(
                p.nombre,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                if (p.precio > 0) p.precio.pesos() else "Sin precio",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        if (p.enLista) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "En la lista",
                tint = color,
                modifier = Modifier.align(Alignment.TopEnd).size(22.dp)
            )
        }
    }
}

// ------------------------------------------------------------
// Pantalla 2: mi lista con precios, cantidades y total
// ------------------------------------------------------------

@Composable
fun ListaScreen(vm: MercadoViewModel) {
    var menu by remember { mutableStateOf(false) }
    val enLista = vm.enLista

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Mi lista",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            if (enLista.isNotEmpty()) {
                Box {
                    IconButton(onClick = { menu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(
                            text = { Text("Desmarcar comprados") },
                            onClick = { vm.desmarcarComprados(); menu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Vaciar lista") },
                            onClick = { vm.vaciarLista(); menu = false }
                        )
                    }
                }
            }
        }

        if (enLista.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Tu lista está vacía",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Ve a Productos y toca los artículos que quieres comprar.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Seccion.entries.forEach { s ->
                    val lista = enLista.filter { it.grupo.seccion == s }
                    if (lista.isNotEmpty()) {
                        item(key = "h_${s.name}") {
                            Row(
                                Modifier.fillMaxWidth().padding(top = 8.dp, start = 4.dp, end = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(s.titulo, fontWeight = FontWeight.Bold, color = s.color)
                                Text(
                                    lista.sumOf { it.subtotal }.pesos(),
                                    fontWeight = FontWeight.Bold,
                                    color = s.color
                                )
                            }
                        }
                        items(lista, key = { it.id }) { p -> FilaLista(p, s.color, vm) }
                    }
                }
            }
            BarraTotal(vm)
        }
    }
}

@Composable
fun FilaLista(p: Producto, color: Color, vm: MercadoViewModel) {
    var textoPrecio by remember(p.id) {
        mutableStateOf(if (p.precio > 0) p.precio.toLong().toString() else "")
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = { vm.modificar(p.id) { it.copy(comprado = !it.comprado) } },
                    modifier = Modifier.size(32.dp)
                ) {
                    if (p.comprado) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Comprado",
                            tint = color,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Box(Modifier.size(24.dp).border(2.dp, Color(0xFFB8C2BA), CircleShape))
                    }
                }
                EmojiCircle(p.emoji, color, 46.dp, 26.sp)
                Column(Modifier.weight(1f)) {
                    Text(
                        p.nombre,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (p.comprado) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (p.comprado) Color.Gray else Color.Unspecified
                    )
                    Text(p.grupo.titulo, fontSize = 12.sp, color = Color.Gray)
                }
                Text(p.subtotal.pesos(), fontWeight = FontWeight.Bold, color = color)
                IconButton(onClick = { vm.quitarDeLista(p.id) }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Quitar de la lista",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = textoPrecio,
                    onValueChange = { nuevo ->
                        val limpio = nuevo.filter { c -> c.isDigit() }.take(9)
                        textoPrecio = limpio
                        vm.modificar(p.id) { it.copy(precio = limpio.toDoubleOrNull() ?: 0.0) }
                    },
                    label = { Text("Precio c/u") },
                    prefix = { Text("$ ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(150.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(onClick = {
                        if (p.cantidad > 1) vm.modificar(p.id) { it.copy(cantidad = it.cantidad - 1) }
                    }) { Text("−", fontSize = 20.sp) }
                    Text(
                        "${p.cantidad}",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(min = 28.dp)
                    )
                    FilledTonalIconButton(onClick = {
                        if (p.cantidad < 99) vm.modificar(p.id) { it.copy(cantidad = it.cantidad + 1) }
                    }) { Icon(Icons.Default.Add, contentDescription = "Más") }
                }
            }
        }
    }
}

@Composable
fun BarraTotal(vm: MercadoViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFF5BB373), Color(0xFF3FA08C))))
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${vm.enLista.size} productos", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
            Text(
                "Comprado: ${vm.totalComprado.pesos()}",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total del mercado",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                vm.total.pesos(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

// ------------------------------------------------------------
// Pantalla 3: nuevo producto
// ------------------------------------------------------------

private fun primerEmoji(s: String): String {
    if (s.isEmpty()) return s
    val bi = BreakIterator.getCharacterInstance()
    bi.setText(s)
    return s.substring(0, bi.next())
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoProductoDialog(
    seccionInicial: Seccion,
    onCerrar: () -> Unit,
    onGuardar: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("🛒") }
    var precio by remember { mutableStateOf("") }
    var seccion by remember { mutableStateOf(seccionInicial) }
    var grupo by remember { mutableStateOf(Grupo.entries.first { it.seccion == seccionInicial }) }
    var agregar by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Nuevo producto") },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = primerEmoji(it) },
                    label = { Text("Imagen (emoji)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { nuevo -> precio = nuevo.filter { c -> c.isDigit() }.take(9) },
                    label = { Text("Precio") },
                    prefix = { Text("$ ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Text("Sección", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Seccion.entries.forEach { s ->
                        FilterChip(
                            selected = seccion == s,
                            onClick = {
                                seccion = s
                                grupo = Grupo.entries.first { g -> g.seccion == s }
                            },
                            label = { Text(s.titulo) }
                        )
                    }
                }

                Text("Grupo", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Grupo.entries.filter { it.seccion == seccion }.forEach { g ->
                        FilterChip(
                            selected = grupo == g,
                            onClick = { grupo = g },
                            label = { Text(g.titulo) }
                        )
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Agregar a mi lista ahora", modifier = Modifier.weight(1f))
                    Switch(checked = agregar, onCheckedChange = { agregar = it })
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = nombre.isNotBlank(),
                onClick = {
                    onGuardar(
                        Producto(
                            nombre = nombre.trim(),
                            emoji = emoji.ifBlank { "🛒" },
                            grupo = grupo,
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            enLista = agregar
                        )
                    )
                }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } }
    )
}
