package com.jsalin.laguiadejerryapp.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsalin.laguiadejerryapp.R
import com.jsalin.laguiadejerryapp.domain.model.CharacterStatus
import com.jsalin.laguiadejerryapp.presentation.theme.StatusAlive
import com.jsalin.laguiadejerryapp.presentation.theme.StatusDead
import com.jsalin.laguiadejerryapp.presentation.theme.StatusUnknown

@Composable
fun StatusLabel(status: CharacterStatus, species: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(8.dp)
                .background(status.color(), CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "${stringResource(status.label())} · $species",
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun CharacterStatus.color() = when (this) {
    CharacterStatus.ALIVE -> StatusAlive
    CharacterStatus.DEAD -> StatusDead
    CharacterStatus.UNKNOWN -> StatusUnknown
}

@StringRes
private fun CharacterStatus.label() = when (this) {
    CharacterStatus.ALIVE -> R.string.status_alive
    CharacterStatus.DEAD -> R.string.status_dead
    CharacterStatus.UNKNOWN -> R.string.status_unknown
}