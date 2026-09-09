//Es un programa que incrementa y decrementa un contador guardando el historial de los cambios
// Jorge Martinez Cambara
//09/09/2026
package dev.jamc.lab6jamr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jamc.lab6jamr.ui.theme.Lab6JaMrTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab6JaMrTheme {
                ventana()

            }
        }
    }
}
@Preview
@Composable
fun ventana() {
    var contador by remember{ mutableIntStateOf(0) }
    var incrementos by remember { mutableIntStateOf(0) }
    var decrementos by remember { mutableIntStateOf(0) }
    var maximo by remember { mutableIntStateOf(0) }
    var minimo by remember { mutableIntStateOf(0) }
    val historial = remember {
        mutableStateListOf<Pair<Int, Boolean>>()
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Jorge Antonio Martínez",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier=Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    contador--
                    decrementos++
                    minimo=minOf(minimo,contador)
                    historial.add(contador to false)
                }) {
                    Text("-", fontSize = 24.sp)
                }

                Text(
                    text = contador.toString(),
                    fontSize=60.sp,
                    modifier=Modifier.padding(horizontal=20.dp)
                )
                Button(onClick = {contador++
                incrementos++
                maximo=maxOf(maximo,contador)
                historial.add(contador to true)}) {
                    Text("+", fontSize = 24.sp)
                }
            }//Se termina el row
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Text(text="Total incrementos: ${incrementos}",fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center)
            Text(text="Total decrementos: $decrementos",fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                )
            Text(text="Valor máximo: $maximo",fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
               )
            Text(text="Valor mínimo: $minimo",fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
               )
            Text(text="Total cambios: ${incrementos + decrementos}",fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                )
            Text(text="HISTORIAL : ",fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                )

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier=Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historial){ movimiento ->
                    Card(
                        colors= CardDefaults.cardColors(
                            containerColor= if (movimiento.second){
                                Color(0xFF168AD2D)
                            }else{
                                Color(0xFFC6281E)
                            }
                        )
                    ) {
                        Text(
                            text = movimiento.first.toString(),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )

                    }

                }//TErmina el Lazy GRID


            }
            Button(
                onClick = {
                    contador = 0
                    incrementos = 0
                    decrementos = 0
                    maximo = 0
                    minimo = 0
                    historial.clear()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reiniciar")
            }


        }
    }


}

