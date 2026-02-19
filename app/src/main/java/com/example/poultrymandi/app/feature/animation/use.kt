package com.example.poultrymandi.app.feature.animation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ─────────────────────────────────────────────────────────────
//  EXAMPLE 1: Music Player Island
//  → collapsed: song name
//  → expanded: full player controls
// ─────────────────────────────────────────────────────────────
@Composable
fun MusicPlayerScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // ... baaki screen content

        DynamicIsland(
            topPadding = 48.dp,
            collapsedContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.FavoriteBorder, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Text("Blinding Lights", color = Color.White, fontSize = 13.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Build, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            },
            expandedContent = { scope ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Now Playing", color = Color.White.copy(0.5f), fontSize = 11.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Blinding Lights", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("The Weeknd", color = Color.White.copy(0.6f), fontSize = 14.sp)
                    Spacer(Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(36.dp))
                        Icon(Icons.Default.Face, null, tint = Color.White, modifier = Modifier.size(48.dp))
                        Icon(Icons.Default.Home, null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { scope.close() }) {
                        Text("Close", color = Color.White.copy(0.5f))
                    }
                }
            }
        )
    }
}


// ─────────────────────────────────────────────────────────────
//  EXAMPLE 2: Live Delivery Tracker Island
//  → collapsed: "Arriving in 8 min 🛵"
//  → expanded: full map + order status
// ─────────────────────────────────────────────────────────────
@Composable
fun DeliveryTrackerScreen() {
    Box(modifier = Modifier.fillMaxSize()) {

        DynamicIsland(
            topPadding = 48.dp,
            collapsedContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🛵", fontSize = 16.sp)
                    Text("Arriving in 8 min", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            },
            expandedContent = { scope ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your Order", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("🛵  Rider is on the way!", color = Color(0xFF34D399), fontSize = 14.sp)
                    Spacer(Modifier.height(24.dp))
                    // Steps
                    listOf("Order Placed ✅", "Preparing ✅", "Out for Delivery 🔄", "Delivered").forEach {
                        Text(it, color = Color.White.copy(0.7f), fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    OutlinedButton(
                        onClick = { scope.close() },
                        colors  = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) { Text("Minimize") }
                }
            }
        )
    }
}



@Composable
fun NotificationIslandScreen() {
    Box(modifier = Modifier.fillMaxSize()) {

        DynamicIsland(
            topPadding     = 48.dp,
            collapsedWidth = 180.dp,
            collapsedContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🔔", fontSize = 14.sp)
                    Text("1 New Alert", color = Color.White, fontSize = 13.sp)
                }
            },
            expandedContent = { scope ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notifications", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { scope.close() }) {
                            Icon(Icons.Default.Close, null, tint = Color.White)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    listOf(
                        "💸 ₹5,000 credited to your account",
                        "🔐 New login from Delhi",
                        "🎉 Cashback of ₹120 unlocked"
                    ).forEach { msg ->
                        Text(
                            text = msg,
                            color = Color.White.copy(0.8f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Divider(color = Color.White.copy(0.1f))
                    }
                }
            }
        )
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDynamicIsland() {
    // Koi bhi ek screen choose karo
    NotificationIslandScreen()
}