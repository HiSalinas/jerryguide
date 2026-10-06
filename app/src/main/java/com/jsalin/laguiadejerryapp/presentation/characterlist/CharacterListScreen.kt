package com.jsalin.laguiadejerryapp.presentation.characterlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.jsalin.laguiadejerryapp.R
import com.jsalin.laguiadejerryapp.domain.model.Character
import com.jsalin.laguiadejerryapp.presentation.components.FullScreenError
import com.jsalin.laguiadejerryapp.presentation.components.FullScreenLoading
import com.jsalin.laguiadejerryapp.presentation.components.toMessageRes
import kotlinx.coroutines.launch

@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterListViewModel = hiltViewModel(),
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    CharacterListContent(
        query = viewModel.query,
        characters = characters,
        onQueryChange = viewModel::onQueryChange,
        onCharacterClick = onCharacterClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListContent(
    query: String,
    characters: LazyPagingItems<Character>,
    onQueryChange: (String) -> Unit,
    onCharacterClick: (Int) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    val refresh = characters.loadState.refresh
    val isEmpty = characters.itemCount == 0

    val refreshError = (refresh as? LoadState.Error)?.error
    val errorMessage = refreshError?.let { stringResource(it.toMessageRes()) }
    val retryLabel = stringResource(R.string.retry)

    LaunchedEffect(refreshError) {
        if (errorMessage == null || isEmpty) return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = errorMessage,
            actionLabel = retryLabel,
            duration = SnackbarDuration.Long,
        )
        if (result == SnackbarResult.ActionPerformed) characters.retry()
    }

    Scaffold(
        topBar = {
            Surface {
                Column {
                    TopAppBar(title = {
                        Text(stringResource(R.string.appbar_title),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier)
                    })
                    SearchField(
                        query = query,
                        onQueryChange = { newQuery ->
                            onQueryChange(newQuery)
                            scope.launch { gridState.scrollToItem(0) }
                        },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        when {
            refresh is LoadState.Loading && isEmpty -> FullScreenLoading(Modifier.padding(innerPadding))
            refreshError != null && isEmpty -> FullScreenError(
                messageRes = refreshError.toMessageRes(),
                onRetry = characters::retry,
                modifier = Modifier.padding(innerPadding),
            )
            refresh is LoadState.NotLoading && isEmpty && query.isNotBlank() -> EmptySearch(
                query = query.trim(),
                modifier = Modifier.padding(innerPadding),
            )
            else -> CharacterGrid(
                characters = characters,
                state = gridState,
                contentPadding = innerPadding,
                onCharacterClick = onCharacterClick,
            )
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text(stringResource(R.string.search_hint)) },
        leadingIcon = { Icon(painterResource(R.drawable.outline_search_24), contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(R.drawable.outline_close_24),
                        contentDescription = stringResource(R.string.clear_search),
                    )
                }
            }
        },
        singleLine = true,
        shape = CircleShape,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
    )
}

@Composable
private fun EmptySearch(query: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.search_empty, query),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CharacterGrid(
    characters: LazyPagingItems<Character>,
    state: LazyGridState,
    contentPadding: PaddingValues,
    onCharacterClick: (Int) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(layoutDirection) + 16.dp,
            end = contentPadding.calculateEndPadding(layoutDirection) + 16.dp,
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = characters.itemCount,
            key = characters.itemKey { it.id },
            contentType = characters.itemContentType { "character" },
        ) { index ->
            characters[index]?.let { character ->
                CharacterCard(
                    character = character,
                    onClick = { onCharacterClick(character.id) },
                )
            }
        }

        when (val append = characters.loadState.append) {
            is LoadState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(append.error.toMessageRes()),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    TextButton(onClick = characters::retry) { Text(stringResource(R.string.retry)) }
                }
            }
            is LoadState.NotLoading -> Unit
        }
    }
}