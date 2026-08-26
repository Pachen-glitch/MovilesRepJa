package com.example.labappja
//Es un programa hecho con Kotlin COmpose
//Que utiliza objetos como Toast, Intent, Action
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.labappja.ui.theme.LabAppJaTheme
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration //Modo oscuro
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton

import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LabAppJaTheme {
                RestaurantScreen()
            }
        }
    }
}

@Preview(showBackground=true)
@Composable
fun RestaurantScreen() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Barra()
            Day()
            Carta()


        }
    }
}




// Primera columna banner de descarga
@Composable
fun Barra(){
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color= MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp

            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = {
                    // ACCIOn
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Buscar actualización"
                )
            }
            Text(text = "Actualizacion Disponible",
                modifier= Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall)
            TextButton(
                onClick = {
                    try {
                        val playStoreIntent = Intent(//
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.whatsapp")
                        )

                        context.startActivity(playStoreIntent)
                    } catch (_: ActivityNotFoundException) {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                "https://play.google.com/store/apps/details?id=com.whatsapp"
                            )
                        )

                        context.startActivity(browserIntent)
                    }
                }
            ){
                Text("DESCARGAR")
            }




        }
    }
}
@Composable
fun Day(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically)
    {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Lunes",
                style = MaterialTheme.typography.headlineLarge)
            Text(text = "13 de abril", style = MaterialTheme.typography.titleLarge)

        }
        OutlinedButton(onClick = {
            //A
        }) { Text(text = "Terminar Jornada")}
    }

}
@Composable
fun Carta(){
    val context = LocalContext.current
    Card(modifier= Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        elevation= CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    )
    {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.Top
                ) {
                Text(text = "Cevicheria Los Chavos",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = {
                        val latitude = 14.624495
                        val longitude = -90.498466
                        val restaurantName = Uri.encode("Cevichería Los Chavos")
                        val locationUri = ("geo:$latitude,$longitude" +
                                "?q=$latitude,$longitude($restaurantName)").toUri()
                        val mapIntent = Intent(
                            Intent.ACTION_VIEW,
                            locationUri
                        ).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        try {
                            context.startActivity(mapIntent)
                        } catch (_: ActivityNotFoundException) {
                            val browserUri = ("https://www.google.com/maps/search/" +
                                    "?api=1&query=$latitude,$longitude").toUri()

                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, browserUri)
                            )
                        }

                    }) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Abrir ubicación",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)

                    )
                }
            }
            Text(text = "35 Avenida 22-81, Cdg 01005")
            Text(text = "10:00 AM 6:00 PM:")
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                onClick = {
                    Toast.makeText(
                        context,
                        "Jorge Martinez Cambara",
                        Toast.LENGTH_SHORT//DURACION
                    ).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Iniciar")
            }

                TextButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Mariscos\nQQ",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Detalles")
                }

            }
        }
    }
}



//PANTALLA CLARA
@Preview(
    name = "Modo claro",
    showBackground = true
)
@Composable
fun RestaurantScreenLightPreview() {
    LabAppJaTheme {
        RestaurantScreen()
    }
}

// Modo oscuro
@Preview(
    name = "Modo oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RestaurantScreenDarkPreview() {
    LabAppJaTheme {
        RestaurantScreen()
    }
}