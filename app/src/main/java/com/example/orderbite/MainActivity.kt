package com.example.orderbite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.orderbite.ui.theme.OrderBiteTheme
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    var orderedItemsCount by mutableStateOf(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderBiteTheme {
                MyApp()
            }
        }
    }
}


class OrderViewModel : ViewModel() {
    var orderedQuantities by mutableStateOf<Map<String, Int>>(emptyMap())
        private set

    fun setOrderedQuantity(itemName: String, quantity: Int) {
        orderedQuantities = orderedQuantities + (itemName to quantity)
    }

    fun getOrderedQuantity(itemName: String): Int {
        return orderedQuantities[itemName] ?: 0
    }
}

data class NavItemState(
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ComposableDestinationInComposeScope")
@Composable
fun MyApp(modifier: Modifier = Modifier, viewModel: OrderViewModel = remember { OrderViewModel() }) {
    val navController = rememberNavController()
    val items = listOf(
        NavItemState(
            title = "Precel",
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.Star,
        ),
        NavItemState(
            title = "Pizza",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite,
        ),
    )

    val pretzelList = listOf(
        "Kurczak koperkowo ziolowy",
        "Cheese",
        "Funghi",
        "Supreme",
        "Spinacio",
        "Salami Hot",
        "Salami Plus"
    )

    val pizzaList = listOf(
        "Margherita",
        "Pepperoni",
        "Capricciosa",
        "Quattro formaggi",
        "Vegetariana",
        "Hawaiian",
        "Diavola"
    )

    var bottomNavState by rememberSaveable {
        mutableStateOf(0)
    }

    var orderedQuantity by rememberSaveable { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "orders") {
        composable("orders") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "OrderBite")
                            }
                        },
                        modifier = modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        actions = {
                            IconButton(onClick = {
                                navController.navigate("ShopScreen")
                            }) {
                                BadgedBox(badge = {
                                    Badge(modifier.size(10.dp)) {}
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ShoppingCart,
                                        contentDescription = "Koszyk"
                                    )
                                }
                            }
                        },
                        colors = topAppBarColors(
                            containerColor = Color(0xFFE0A9A5)
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        containerColor = Color(0xFFE0A9A5)
                    ) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = bottomNavState == index,
                                onClick = {
                                    bottomNavState = index
                                    if (index == 3) {
                                        navController.navigate("ShoppingCart")
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (bottomNavState == index) item.selectedIcon
                                        else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                },
                                label = {
                                    Text(text = item.title)
                                }
                            )
                        }
                    }
                }
            ) { contentPadding ->
                LazyColumn(
                    modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        if (bottomNavState == 0) {
                            pretzelList.forEachIndexed { index, pretzelName ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    PretzelItem(pretzelName, viewModel)
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        } else if (bottomNavState == 1) {
                            pizzaList.forEachIndexed { index, pizzaName ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    PizzaItem(pizzaName, viewModel)
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        } else {
                            // Display content for other tabs as needed
                            Text(
                                text = items[bottomNavState].title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 44.sp
                            )
                        }
                    }
                }
            }
        }
        composable("ShopScreen") {
            ShopScreen(navController, viewModel)
        }
        composable("PreparationTimeScreen") {
            PreparationTimeScreen(navController, viewModel, pizzaList, pretzelList)
        }

    }
}

@Composable
fun PizzaItem(pizzaName: String, viewModel: OrderViewModel) {
    var orderedQuantity by remember { mutableStateOf(viewModel.getOrderedQuantity(pizzaName)) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = pizzaName,
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
                val imageName = pizzaName.replace(" ", "").lowercase()
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
                        viewModel.setOrderedQuantity(pizzaName, orderedQuantity)
                    },
                    modifier = Modifier.size(32.dp),
                    icon = { Text(text = "-") }
                )

                Text(text = orderedQuantity.toString(), fontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 10.dp))

                CircularIconButton(
                    onClick = {
                        orderedQuantity++
                        viewModel.setOrderedQuantity(pizzaName, orderedQuantity)
                    },
                    modifier = Modifier.size(32.dp),
                    icon = { Text(text = "+") }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = getOpisPizza(pizzaName),
            textAlign = TextAlign.Justify,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PretzelItem(pretzelName: String, viewModel: OrderViewModel) {
    var orderedQuantity by remember { mutableStateOf(viewModel.getOrderedQuantity(pretzelName)) }

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

@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(color = Color(0xFFE0A9A5), shape = CircleShape)
    ) {
        icon()
    }
}


@Composable
fun getResourceIdByName(resName: String, context: Context): Int {
    return context.resources.getIdentifier(resName, "drawable", context.packageName)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShopScreen(navController: NavController, viewModel: OrderViewModel) {
    val orderedItems = viewModel.orderedQuantities.filterValues { it > 0 }

    // Nowe pole tekstowe dla adresu
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
                colors = topAppBarColors(
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
    var remainingTime by remember { mutableStateOf(totalPreparationTime * 60) }

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
                colors = topAppBarColors(
                    containerColor = Color(0xFFE0A9A5)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
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



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OrderBiteTheme {
        MyApp()
    }
}