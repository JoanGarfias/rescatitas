package com.example.rescatitas.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rescatitas.AuthState
import com.example.rescatitas.AuthViewModel
import com.example.rescatitas.Models.RegisterUserRequest
import com.example.rescatitas.R

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    // Form fields
    var nombre by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var codigoPostal by remember { mutableStateOf("") }
    var colonia by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Selecciona un estado") }
    var expanded by remember { mutableStateOf(false) }
    
    val estadosMexico = listOf(
        "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
        "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango", "Estado de México",
        "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit",
        "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
        "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas"
    )
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()
            onRegisterSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Green Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color(0xFF4CAF50))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 80.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // White Icon Circle
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.animals),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Text(
                text = "Únete a Rescatitas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
            
            Text(
                text = "Crea una cuenta para empezar a ayudar a los animales que lo necesitan.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    
                    SectionHeader(icon = Icons.Default.Person, title = "Datos Personales")
                    
                    RegisterField(label = "Nombre", value = nombre, onValueChange = { nombre = it }, placeholder = "Ingresa tu nombre")
                    RegisterField(label = "Fecha de Nacimiento", value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, placeholder = "dd/mm/aaaa")
                    RegisterField(label = "Apellido Paterno", value = apellidoPaterno, onValueChange = { apellidoPaterno = it }, placeholder = "Primer apellido")
                    RegisterField(label = "Apellido Materno", value = apellidoMaterno, onValueChange = { apellidoMaterno = it }, placeholder = "Segundo apellido")
                    RegisterField(label = "Teléfono", value = telefono, onValueChange = { telefono = it }, placeholder = "+52 10 dígitos")

                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(icon = Icons.Default.LocationOn, title = "Ubicación")
                    
                    RegisterField(label = "Código Postal", value = codigoPostal, onValueChange = { codigoPostal = it }, placeholder = "5 dígitos")
                    RegisterField(label = "Colonia", value = colonia, onValueChange = { colonia = it }, placeholder = "Ej. Centro")
                    RegisterField(label = "Municipio / Delegación", value = municipio, onValueChange = { municipio = it }, placeholder = "Ej. Guadalajara")
                    
                    Text("Estado", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Box {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(estado, color = if (estado == "Selecciona un estado") Color.Gray else Color.Black)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth(0.8f).heightIn(max = 300.dp)
                        ) {
                            estadosMexico.forEach { nombreEstado ->
                                DropdownMenuItem(
                                    text = { Text(nombreEstado) },
                                    onClick = {
                                        estado = nombreEstado
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(icon = Icons.Default.Lock, title = "Seguridad")
                    
                    RegisterField(label = "Email", value = email, onValueChange = { email = it }, placeholder = "correo@ejemplo.com")
                    RegisterField(label = "Contraseña", value = password, onValueChange = { password = it }, placeholder = "Mínimo 8 caracteres", isPassword = true)
                    RegisterField(label = "Confirmar Contraseña", value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Repite tu contraseña", isPassword = true)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val request = RegisterUserRequest(
                                nombre = nombre,
                                apellido_paterno = apellidoPaterno,
                                apellido_materno = apellidoMaterno,
                                telefono = telefono,
                                codigo_postal = codigoPostal,
                                colonia = colonia,
                                municipio = municipio,
                                estado = estado,
                                fecha_nacimiento = fechaNacimiento,
                                email = email,
                                password = password,
                                password_confirmation = confirmPassword,
                                device_name = "android_device"
                            )
                            viewModel.register(request)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        enabled = authState !is AuthState.Loading
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Crear Cuenta", fontSize = 16.sp)
                        }
                    }
                    
                    TextButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("¿Ya tienes una cuenta? Inicia sesión", color = Color(0xFF00695C), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Icon(icon, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF00695C))
    }
}

@Composable
fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F5F9),
                focusedContainerColor = Color(0xFFF1F5F9),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}
