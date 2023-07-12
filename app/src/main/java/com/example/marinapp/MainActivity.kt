@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.marinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marinapp.ui.theme.MarinappTheme
import java.text.DecimalFormat
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material.MaterialTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarinappTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    // Definindo variáveis mutáveis para a entrada do usuário
    val (Peso, setPeso) = remember { mutableStateOf("") }
    val (TIG, setTIG) = remember { mutableStateOf("") }
    val (Na, setNa) = remember { mutableStateOf("") }
    val (K, setK) = remember { mutableStateOf("") }
    val (Ca, setCa) = remember { mutableStateOf("") }
    val (Mg, setMg) = remember { mutableStateOf("") }

    val (resultPeso, setResultPeso) = remember { mutableStateOf("") }
    val (resultTaxa, setResultTaxa) = remember { mutableStateOf("") }
    val (resultNa, setResultNa) = remember { mutableStateOf("") }
    val (resultK, setResultK) = remember { mutableStateOf("") }
    val (resultCa, setResultCa) = remember { mutableStateOf("") }
    val (resultMg, setResultMg) = remember { mutableStateOf("") }

    val (resultTIG, setResultTIG) = remember { mutableStateOf("") }
    val (resultSG, setResultSG) = remember { mutableStateOf("") }
    val (resultGli25, setResultGli25) = remember { mutableStateOf("") }
    val (resultGli50, setResultGli50) = remember { mutableStateOf("") }

    val (error, setError) = remember { mutableStateOf(false) }
    val df = DecimalFormat("0.##") // Define o formato para duas casas decimais
    val controller = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "MarinApp - Cálculo de Venóclise",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    // Text fields para a primeira coluna de entrada do usuário
                    OutlinedTextField(
                        value = Peso,
                        onValueChange = { setPeso(it) },
                        label = { Text("Peso") },
                        modifier = Modifier.padding(bottom = 8.dp, end = 16.dp)
                    )
                    OutlinedTextField(
                        value = TIG,
                        onValueChange = { setTIG(it) },
                        label = { Text("TIG") },
                        modifier = Modifier.padding(bottom = 8.dp, end = 16.dp)
                    )
                    OutlinedTextField(
                        value = Na,
                        onValueChange = { setNa(it) },
                        label = { Text("Na") },
                        modifier = Modifier.padding(bottom = 8.dp, end = 16.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    // Text fields para a segunda coluna de entrada do usuário
                    OutlinedTextField(
                        value = K,
                        onValueChange = { setK(it) },
                        label = { Text("K") },
                        modifier = Modifier.padding(bottom = 8.dp, start = 16.dp)
                    )
                    OutlinedTextField(
                        value = Ca,
                        onValueChange = { setCa(it) },
                        label = { Text("Ca") },
                        modifier = Modifier.padding(bottom = 8.dp, start = 16.dp)
                    )
                    OutlinedTextField(
                        value = Mg,
                        onValueChange = { setMg(it) },
                        label = { Text("Mg") },
                        modifier = Modifier.padding(bottom = 8.dp, start = 16.dp)
                    )
                }
            }

            // Botão para realizar a operação de adição
            Button(
                onClick = {
                    val inputs = listOf(Peso, TIG, Na, K, Ca, Mg)
                    val numbers = inputs.mapNotNull { it.toDoubleOrNull() }

                    if (numbers.size == inputs.size) {

                        val peso = numbers[0]
                        val tig = numbers[1]
                        val na = numbers[2]
                        val k = numbers[3]
                        val ca = numbers[4]
                        val mg = numbers[5]

                        var rTaxa: Double = if (peso <= 10) {
                            peso * 100
                        } else if (peso <= 20) {
                            1000 + (peso - 10) * 50
                        } else {
                            1500 + (peso - 20) * 20
                        }

                        var rPeso = rTaxa / 100
                        var rNa = na * rPeso / 3.4
                        var rK = k * rPeso / 2.5
                        var rCa = ca * rPeso / 0.5
                        var rMg = mg * rPeso / 4

                        var rTIG = tig * rPeso * 1.44

                        var rSG: Double = if (rPeso == 0.0) {
                            0.0
                        } else if (rTIG * 100 / 5 < rTaxa) {
                            rTIG * 100 / 5
                        } else if ((rTIG * 100 / 5 - rTaxa) <= 130) {
                            rTaxa - 50
                        } else if ((rTIG * 100 / 5 - rTaxa) <= 700) {
                            rTaxa - 100
                        } else if ((rTIG * 100 / 5 - rTaxa) <= 1500) {
                            rTaxa - 200
                        } else {
                            0.0
                        }

                        var rGli25: Double = if ((rTIG - (rSG * 5 / 100)) * 100 / 25 < 100) {
                            (rTIG - (rSG * 5 / 100)) * 100 / 25
                        } else {
                            0.0
                        }

                        var rGli50: Double = if (((rTIG - (rSG * 5 / 100)) * 100 / 25) > 100) {
                            (rTIG - (rSG * 5 / 100)) * 100 / 50
                        } else {
                            0.0
                        }

                        setResultPeso(df.format(rPeso).toString())
                        setResultTaxa(df.format(rTaxa).toString())
                        setResultNa(df.format(rNa).toString())
                        setResultK(df.format(rK).toString())
                        setResultCa(df.format(rCa).toString())
                        setResultMg(df.format(rMg).toString())

                        setResultTIG(df.format(rTIG).toString())
                        setResultSG(df.format(rSG).toString())
                        setResultGli25(df.format(rGli25).toString())
                        setResultGli50(df.format(rGli50).toString())

                        setError(false)
                    } else {
                        setError(true)
                    }
                    controller?.hide()
                },
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally).width(200.dp).height(50.dp)
            ) {
                Text("Calcular")
            }

            // Exibindo mensagem de erro se houver entradas inválidas
            if (error) {
                Text(
                    text = "Por favor, insira números válidos em todos os campos. Lembre-se de utilizar o ponto (.) para números decimais.",
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else if (resultPeso.isNotBlank() && resultTaxa.isNotBlank() && resultNa.isNotBlank() && resultK.isNotBlank() && resultCa.isNotBlank() && resultMg.isNotBlank()) {
                // Texto para mostrar os resultados em formato de tabela
                Text(text = "Hidratação Parenteral", modifier = Modifier.padding(top = 16.dp))
                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Peso calórico")
                        Text(text = "Taxa Hídrica")
                        Text(text = "Na")
                        Text(text = "K")
                        Text(text = "Ca")
                        Text(text = "Mg")

                        Text(text = "TIG (g)")
                        Text(text = "SG 5%")
                        Text(text = "Glicose 25%")
                        Text(text = "Glicose 50%")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = ": $resultPeso")
                        Text(text = ": $resultTaxa")
                        Text(text = ": $resultNa")
                        Text(text = ": $resultK")
                        Text(text = ": $resultCa")
                        Text(text = ": $resultMg")

                        Text(text = ": $resultTIG")
                        Text(text = ": $resultSG")
                        Text(text = ": $resultGli25")
                        Text(text = ": $resultGli50")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    MarinappTheme {
        MyApp()
    }
}
