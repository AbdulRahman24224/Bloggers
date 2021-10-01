package com.example.bloggers.base.utils.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun sP(x: Int) = Spacer(modifier = Modifier.padding(x.dp))

@Composable
fun sH(x: Int) = Spacer(modifier = Modifier.requiredHeight(x.dp))

@Composable
fun sW(x: Int) = Spacer(modifier = Modifier.requiredWidth(x.dp))