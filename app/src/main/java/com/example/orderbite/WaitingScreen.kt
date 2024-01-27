package com.example.orderbite

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreparationTimeScreen(
    navController: NavController,
    viewModel: OrderViewModel,
    pizzaList: List<String>,
    pretzelList: List<String>
) {
    val pizzasTime = viewModel.orderedQuantities.filterKeys { it in pizzaList }.values.sum() * 20
    val pretzelsTime = viewModel.orderedQuantities.filterKeys { it in pretzelList }.values.sum() * 10
    val deliveryTime = 20

    val totalPreparationTime = pizzasTime + pretzelsTime + deliveryTime
    var remainingTime by remember { mutableIntStateOf(totalPreparationTime * 60) }

    DisposableEffect(Unit) {
        val timer = object : CountDownTimer((remainingTime * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                navController.navigate("NextScreen")
            }
        }
        timer.start()

        onDispose {
            timer.cancel()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Odbiór")
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE0A9A5)
                ),
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (remainingTime > 0) {
                    Text(
                        text = "Twoje zamówienie jest gotowe do odbioru za ${(remainingTime / 60)} minut ${remainingTime % 60} sekund!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Green
                    )
                } else {
                    Text(
                        text = "Twoje zamówienie jest gotowe do odbioru!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Green
                    )
                }
            }
        }
    )
}

