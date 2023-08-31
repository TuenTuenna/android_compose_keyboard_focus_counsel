package com.example.uipractice.ui.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import com.example.uipractice.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(modifier: Modifier) {
    val scope = rememberCoroutineScope()

    val animateState = remember { mutableStateOf(false) }

    val bottomScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomScaffoldState,
        topBar = {
            CenterAlignedTopAppBar(title = {
                MonthWeekToggleSwitch(
                    width = 105, height = 35, animateState = animateState
                )
            }, navigationIcon = {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 21.dp)
                        .clickable {
                        },
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "menubar"
                )
            })
        },
        sheetContent = {}) {
        TodoList(paddingValues = it)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoList(modifier: Modifier = Modifier, paddingValues: PaddingValues){

    val listState = rememberLazyListState()

    var userInput by remember { mutableStateOf(TextFieldValue("")) }

    var scope = rememberCoroutineScope()

    LazyColumn(
        contentPadding = PaddingValues(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 200.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
        ,
        state = listState,
        reverseLayout = false,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            TextField(value = userInput, onValueChange = { userInput = it })
        }
        items(10) {
            TodoRow(name = "호롤롤로")
        }
        item {
            TextField(value = userInput, onValueChange = { userInput = it },
                modifier = Modifier.onFocusChanged {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                })
        }
        items(10) {
            TodoRow(name = "호롤롤로")
        }


    }
}

@Composable
fun TodoRow(name: String) {
    Text(text = "Hello $name!")
}


@OptIn(ExperimentalMotionApi::class)
@Composable
fun MonthWeekToggleSwitch(
    width: Int, height: Int, animateState: MutableState<Boolean>
) {
    val progressState by animateFloatAsState(
        targetValue = if (animateState.value) {
            1f
        } else {
            0f
        }
    )

    val toggleSwitchMonthText by animateColorAsState(
        targetValue = if (animateState.value) {
            Color(0xFF9E9E9E) // when on
        } else {
            Color.Black // when off
        },
    )

    val toggleSwitchWeekText by animateColorAsState(
        targetValue = if (animateState.value) {
            Color.Black // when on
        } else {
            Color(0xFF9E9E9E) // when off
        },
    )

    MotionLayout(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(7.dp)),
        start = startConstraintsSet(parentWidth = width, parentHeight = height),
        end = endConstraintsSet(parentWidth = width, parentHeight = height),
        progress = progressState
    ) {
        Box(
            modifier = Modifier
                .width(115.dp)
                .height(35.dp)
                .clip(RoundedCornerShape(7.dp))
                .layoutId("ToggleSwitchBackground")
                .background(Color(0xffe9e9e9))
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        animateState.value = !animateState.value
                    })
                }
        )

        Box(
            modifier = Modifier
                .layoutId("ToggleSwitch")
                .padding(2.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .layoutId("text_month"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "월간",
                color = toggleSwitchMonthText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
        }

        Box(
            modifier = Modifier
                .layoutId("text_week"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "주간",
                color = toggleSwitchWeekText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

//for Month State
private fun startConstraintsSet(
    parentWidth: Int, parentHeight: Int
): ConstraintSet {
    return ConstraintSet {
        val ToggleSwitchBackground = createRefFor("ToggleSwitchBackground")
        val ToggleSwitch = createRefFor("ToggleSwitch")
        val text_month = createRefFor("text_month")
        val text_week = createRefFor("text_week")

        constrain(ToggleSwitchBackground) {
            width = Dimension.value(parentWidth.dp)
            height = Dimension.value(parentHeight.dp)
        }

        //ToggleSwitch: left side default
        constrain(ToggleSwitch) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_month(월간): left side default
        constrain(text_month) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_week(주간): left side default
        constrain(text_week) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }
    }
}

//for Week State
private fun endConstraintsSet(
    parentWidth: Int,
    parentHeight: Int
): ConstraintSet {
    return ConstraintSet {
        val ToggleSwitchBackground = createRefFor("ToggleSwitchBackground")
        val ToggleSwitch = createRefFor("ToggleSwitch")
        val text_month = createRefFor("text_month")
        val text_week = createRefFor("text_week")

        constrain(ToggleSwitchBackground) {
            width = Dimension.value(parentWidth.dp)
            height = Dimension.value(parentHeight.dp)
        }

        //ToggleSwitch: right side default
        constrain(ToggleSwitch) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_month(월간): right side default
        constrain(text_month) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_week(주간): right side default
        constrain(text_week) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }
    }
}
