package com.example.orderbite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.orderbite.ui.theme.OrderBiteTheme

class MainActivity : ComponentActivity() {
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
        mutableIntStateOf(0)
    }


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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OrderBiteTheme {
        MyApp()
    }
}