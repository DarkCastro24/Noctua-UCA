package com.castroll.noctua

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.castroll.noctua.data.local.UserViewModel
import com.castroll.noctua.ui.firstuse.FirstUseActivity
import com.castroll.noctua.di.MyApp
import com.castroll.noctua.ui.theme.MyApplicationTheme
import com.castroll.noctua.ui.home.HomeScreen
import com.castroll.noctua.ui.laboratories.LaboratoriesScreen
import com.castroll.noctua.ui.maps.MapsScreen
import com.castroll.noctua.ui.pensum.PensumScreen
import com.castroll.noctua.ui.profile.ProfileScreen
import com.castroll.noctua.ui.qr.QRScreen
import com.castroll.noctua.ui.search.SearchScreen
import com.castroll.noctua.ui.signout.SignOutScreen
import com.castroll.noctua.ui.search.SearchViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by lazy {
        (application as MyApp).viewModelProvider[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel.user.observe(this) { user ->
            if (user?.biography.isNullOrEmpty() || user?.hobbies.isNullOrEmpty()) {
                val intent = Intent(this, FirstUseActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                setContent {
                    MyApplicationTheme {
                        MainScreen(userViewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Cargar la fuente personalizada
    val outfitRegular = FontFamily(Font(R.font.outfitregular))

    ModalNavigationDrawer(
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(Color.White)
            ) {
                DrawerContent(navController, userViewModel, outfitRegular) {
                    scope.launch { drawerState.close() }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Noctua UCA", style = MaterialTheme.typography.bodyMedium.copy(fontFamily = outfitRegular)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    actions = {
                        Icon(
                            painter = painterResource(id = R.drawable.imagen2222),
                            contentDescription = "App Logo",
                            tint = Color.White,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 16.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF001f3f),
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            NavigationHost(navController, Modifier.padding(innerPadding), userViewModel, context, outfitRegular)
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController, userViewModel: UserViewModel, outfitRegular: FontFamily, onClose: () -> Unit) {
    val user by userViewModel.user.observeAsState()
    val username = user?.username ?: "Desconocido"
    val userType = user?.type ?: 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_back),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Noctua UCA",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = outfitRegular),
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.imagen2222),
                        contentDescription = "App Logo",
                        tint = Color.White,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$username",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall.copy(fontFamily = outfitRegular),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            DrawerItem("Inicio", R.drawable.icon_home, navController, "home", onClose, outfitRegular)
            DrawerItem("Busqueda", R.drawable.icon_search, navController, "search", onClose, outfitRegular)
            DrawerItem("Laboratorios", R.drawable.icon_laboratories, navController, "laboratories", onClose, outfitRegular)
            DrawerItem("Perfil", R.drawable.icon_profile, navController, "profile", onClose, outfitRegular)
            DrawerItem("Mapas", R.drawable.icon_maps, navController, "maps", onClose, outfitRegular)
            if (userType != 0) {
                DrawerItem("QR", R.drawable.icon_qr, navController, "qr", onClose, outfitRegular)
                DrawerItem("Pensum", R.drawable.icon_pensum, navController, "pensum", onClose, outfitRegular) // Cambiar icono posteriormente
            }
        }
        DrawerItem("Cerrar Sesión", R.drawable.icon_signout, navController, "signout", onClose, outfitRegular, Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp))
    }
}

@Composable
fun DrawerItem(label: String, iconResId: Int, navController: NavHostController, route: String, onClick: () -> Unit, outfitRegular: FontFamily, modifier: Modifier = Modifier) {
    NavigationDrawerItem(
        label = { Text(label, style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular), fontSize = 12.sp) },
        icon = { Icon(painterResource(id = iconResId), contentDescription = label, tint = Color.DarkGray, modifier = Modifier.size(20.dp)) },
        selected = false,
        onClick = {
            onClick()
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 0.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    )
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    context: Context,
    outfitRegular: FontFamily // Recibe la fuente como parámetro
) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen() }
        composable("search") {
            val searchViewModel: SearchViewModel = viewModel()
            SearchScreen(searchViewModel = searchViewModel, userViewModel = userViewModel)
        }
        composable("laboratories") { LaboratoriesScreen() }
        composable("profile") { ProfileScreen(userViewModel = userViewModel) }
        composable("maps") { MapsScreen() }
        composable("qr") { QRScreen(userViewModel = userViewModel) }
        composable("pensum") { PensumScreen(userViewModel = userViewModel) }
        composable("signout") { SignOutScreen(context) }
    }
}
