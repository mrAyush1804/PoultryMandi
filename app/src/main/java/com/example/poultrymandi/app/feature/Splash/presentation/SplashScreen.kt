package com.example.poultrymandi.app.feature.Splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.animation.TwinLinesAnimationWrapper
import com.example.poultrymandi.app.Core.ui.theme.BlackC
import com.example.poultrymandi.app.Core.ui.theme.Coustemyellow
import com.example.poultrymandi.app.Core.ui.theme.brown


@Composable
fun SplashScreen(

) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Coustemyellow )
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Image(
                painter = painterResource(id = R.drawable.chicks),
                contentDescription = "logo",
                modifier = Modifier.size(100.dp)

            )
            Spacer(Modifier.padding(vertical = 5.dp))
            Text(
                text = "PoultryMandi",
                color = Color(0xFF020202),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    fontFamily = MaterialTheme.typography.headlineLarge.fontFamily
                )
            )
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun pre() {
    TwinLinesAnimationWrapper(
        content = {
            SplashScreen()
        },
        Logo = 1
    )


}