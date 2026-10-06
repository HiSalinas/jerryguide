package com.jsalin.laguiadejerryapp.presentation.characterdetail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jsalin.laguiadejerryapp.R
import com.jsalin.laguiadejerryapp.domain.model.CharacterDetail
import com.jsalin.laguiadejerryapp.domain.model.CharacterGender
import com.jsalin.laguiadejerryapp.presentation.components.FullScreenError
import com.jsalin.laguiadejerryapp.presentation.components.FullScreenLoading
import com.jsalin.laguiadejerryapp.presentation.components.StatusLabel
import com.jsalin.laguiadejerryapp.presentation.components.toMessageRes

@Composable
fun CharacterDetailScreen(
    onBack: () -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CharacterDetailContent(uiState = uiState, onBack = onBack, onRetry = viewModel::retry)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailContent(
    uiState: CharacterDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is CharacterDetailUiState.Success) {
                        Text(uiState.detail.character.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            CharacterDetailUiState.Loading -> FullScreenLoading(Modifier.padding(innerPadding))
            is CharacterDetailUiState.Error -> FullScreenError(
                messageRes = uiState.error.toMessageRes(),
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding),
            )
            is CharacterDetailUiState.Success -> CharacterDetailBody(uiState.detail, innerPadding)
        }
    }
}

@Composable
private fun CharacterDetailBody(detail: CharacterDetail, contentPadding: PaddingValues) {
    val character = detail.character
    val unknown = stringResource(R.string.unknown)

    LazyColumn(contentPadding = contentPadding) {
        item {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            )
        }
        item {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusLabel(status = character.status, species = character.species)
                InfoRow(R.string.gender, stringResource(character.gender.label()))
                character.type?.let { InfoRow(R.string.type, it) }
                InfoRow(R.string.origin, character.origin ?: unknown)
                InfoRow(R.string.location, character.location ?: unknown)
            }
        }
        item {
            Text(
                text = stringResource(R.string.episodes_count, detail.episodes.size),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        items(detail.episodes, key = { it.id }) { episode ->
            ListItem(
                overlineContent = { Text(episode.code) },
                headlineContent = { Text(episode.name) },
                supportingContent = { Text(episode.airDate) },
            )
        }
    }
}

@Composable
private fun InfoRow(@StringRes labelRes: Int, value: String) {
    Row {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(96.dp),
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@StringRes
private fun CharacterGender.label() = when (this) {
    CharacterGender.FEMALE -> R.string.gender_female
    CharacterGender.MALE -> R.string.gender_male
    CharacterGender.GENDERLESS -> R.string.gender_genderless
    CharacterGender.UNKNOWN -> R.string.unknown
}