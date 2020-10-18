package com.pratclot.oxygentracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.pratclot.oxygentracker.ui.typography

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NewsStory()
        }
    }

}

@Preview
@Composable
private fun DefaultPreview() {
    NewsStory()
}

@Composable
private fun NewsStory() {
    val image = imageResource(id = R.drawable.header)

    Column(
            modifier = Modifier.padding(16.dp)
    ) {
        val imageModifier = Modifier
                .preferredHeight(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))

        Image(
                image,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.preferredHeight(16.dp))

        Text("My Lord, my Teacher, my Guide, my Protector, my Savior",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = typography.h6)
        Text("My sincere apologies",
                style = typography.body2)
        Text("You are the highest",
                style = typography.body2)
    }
}