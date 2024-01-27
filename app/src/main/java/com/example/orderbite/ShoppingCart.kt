package com.example.orderbite

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalCoilApi::class
)
@Composable
fun ShopScreen(navController: NavController, viewModel: OrderViewModel) {
    val orderedItems = viewModel.orderedQuantities.filterValues { it > 0 }

    var table by rememberSaveable { mutableStateOf("") }
    var showTableWarning by remember { mutableStateOf(false) }
    var orderComments by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Zamówienie")
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE0A9A5)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    stickyHeader {
                        Text(
                            text = "Twoje zamówienie",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }

                    if (orderedItems.isEmpty()) {
                        item {
                            Text(
                                text = "Brak zamówień",
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            )
                        }
                    } else {
                        items(orderedItems.entries.toList()) { (itemName, quantity) ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$itemName $quantity",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .background(color = Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val imageName = itemName.replace(" ", "").lowercase()
                                    val imagePainter = rememberImagePainter(
                                        data = getResourceIdByName(
                                            imageName,
                                            context = LocalContext.current
                                        )
                                    )
                                    Image(
                                        painter = imagePainter,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = table,
                                onValueChange = {
                                    table = it.filter { it.isDigit() }
                                    showTableWarning = false
                                },
                                label = { Text("Numer stolika") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                    item {
                        OutlinedTextField(
                            value = orderComments,
                            onValueChange = {
                                orderComments = it
                            },
                            label = { Text("Uwagi do zamówienia") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }

                    item {
                        Button(
                            onClick = {
                                if (table.isBlank() && orderedItems.isNotEmpty()) {
                                    showTableWarning = true
                                } else if (table.isNotBlank()) {
                                    navController.navigate("PreparationTimeScreen")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFE0A9A5)),
                            enabled = orderedItems.isNotEmpty()
                        ) {
                            Text(text = "Zamów", color = Color.White)
                        }
                    }

                    if (showTableWarning) {
                        item {
                            Text(
                                text = "Podaj numer stolika, do którego ma być doniesione zamówienie!",
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}
