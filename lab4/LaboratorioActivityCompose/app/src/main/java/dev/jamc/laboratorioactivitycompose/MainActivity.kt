//Jorge Martinez Cambara
// 19/08/2026
//Utiliza compose para el visual usando esta tecnologia en ves de xml.
//https://github.com/Pachen-glitch/MovilesRepJa
package dev.jamc.laboratorioactivitycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jamc.laboratorioactivitycompose.ui.theme.LaboratorioActivityComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LaboratorioActivityComposeTheme {
                PortadaLaboratorio()
            }
        }
    }
}

@Composable
fun PortadaLaboratorio() {

    // Fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 50.dp,
                bottom = 24.dp
            )
    ) {

        // CAJA con el borde verde
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 4.dp,
                    color = Color(0xFF006B35)
                )
                .padding(24.dp)
        ) {

            // Imagen de la uvg
            Image(
                painter = painterResource(
                    id = R.drawable.logo_uvg
                ),
                contentDescription = "Escudo de la UVG",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.Center)
                    .alpha(0.12f)
            )

            // CONTENIDO
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    text = "Universidad del Valle\nde Guatemala",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    lineHeight = 34.sp
                )

                Spacer(
                    modifier = Modifier.height(26.dp)
                )


                Text(
                    text = "Programación de plataformas\nmóviles, Sección 30",
                    fontSize = 21.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    lineHeight = 27.sp
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {

                    Text(
                        text = "INTEGRANTES",
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        // INTEGRANTES
                        text = " Valeria \n" +
                                "Jp \n" +
                                "Miguel De Cervantes ",
                        modifier = Modifier.weight(1.3f),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        lineHeight = 21.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "CATEDRÁTICO",
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Juan Carlos Durini",
                        modifier = Modifier.weight(1.3f),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                Spacer(
                    modifier = Modifier.height(30.dp)
                )


                Text(

                    text = "Jorge Martinez Cambara \n25556",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PortadaLaboratorioPreview() {

    LaboratorioActivityComposeTheme {
        PortadaLaboratorio()
    }
}