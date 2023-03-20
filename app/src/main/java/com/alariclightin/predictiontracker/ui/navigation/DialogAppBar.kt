package com.alariclightin.predictiontracker.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.alariclightin.predictiontracker.R
import com.alariclightin.predictiontracker.ui.TestTagConsts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = titleRes),
                modifier = modifier.testTag(TestTagConsts.TopAppBarText)
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        }
    )
}