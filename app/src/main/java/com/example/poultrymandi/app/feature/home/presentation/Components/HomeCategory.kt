package com.example.poultrymandi.app.feature.home.presentation.Components

import android.R.attr.category
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poultrymandi.R
import com.example.poultrymandi.app.Core.ui.theme.brown
import com.example.poultrymandi.app.feature.home.data.Enums.Category
import com.example.poultrymandi.app.feature.home.domain.data.CategoryDomain


@Composable
fun HomeCategory(
    categories: List<CategoryDomain>,
    selectedCategory: CategoryDomain?,
    onCategorySelected: (CategoryDomain) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F7F7))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        categories.forEach { categories ->
            CategoryChip(
                category = categories,
                isSelected = categories.id== selectedCategory?.id,
                onClick = { onCategorySelected(categories) }
            )
        }

    }


}

@Composable
fun CategoryChip(
    category: CategoryDomain,
    isSelected: Boolean,
    onClick: () -> Unit) {

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        label = "categoryBg"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "categoryScale"
    )


    val contentColor by animateColorAsState(
        targetValue = if (isSelected) brown else Color.Gray,
        label = "categoryContent"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .then(
                if (isSelected) Modifier.border(
                    width = 1.dp,
                    color = brown.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {

        Icon(
            painter = painterResource(id = category.icon),
            contentDescription = category.title,
            tint = Color.Unspecified,  // Original icon color
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = category.title,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeCategoryPreview() {
    val dummyCategories = listOf(
        CategoryDomain("eggs", "Eggs", R.drawable.egg_tongue_face),
        CategoryDomain("chicken", "Chicken", R.drawable.boiled_chicken),
        CategoryDomain("broiler", "Broiler", R.drawable.chicken),
    )
    Column(Modifier.padding(16.dp)) {
        HomeCategory(
            categories = dummyCategories,
            selectedCategory = dummyCategories[0],
            onCategorySelected = {category}
        )
    }
}
