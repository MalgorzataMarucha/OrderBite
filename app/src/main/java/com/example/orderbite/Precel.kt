package com.example.orderbite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PretzelItem(pretzelName: String, viewModel: OrderViewModel) {
    var orderedQuantity by remember { mutableIntStateOf(viewModel.getOrderedQuantity(pretzelName)) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = pretzelName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                val imageName = pretzelName.replace(" ", "").lowercase()
                val imagePainter = rememberImagePainter(
                    data = getResourceIdByName(imageName, context = LocalContext.current)
                )
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularIconButton(
                    onClick = {
                        orderedQuantity = maxOf(orderedQuantity - 1, 0)
                        viewModel.setOrderedQuantity(pretzelName, orderedQuantity)
                    },
                    modifier = Modifier.size(32.dp),
                    icon = { Text(text = "-") }
                )

                Text(text = orderedQuantity.toString(), fontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 10.dp))

                CircularIconButton(
                    onClick = {
                        orderedQuantity++
                        viewModel.setOrderedQuantity(pretzelName, orderedQuantity)
                    },
                    modifier = Modifier.size(32.dp),
                    icon = { Text(text = "+") }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = getOpisPrecel(pretzelName),
            textAlign = TextAlign.Justify,
            fontSize = 16.sp
        )
    }
}