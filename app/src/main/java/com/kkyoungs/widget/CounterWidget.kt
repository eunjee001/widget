package com.kkyoungs.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

object CounterWidget:GlanceAppWidget() {
    val countKey = intPreferencesKey("count")

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        /**
         * 위젯을 만들기 위해서는 GlanceAppWidget을 상속받아서 provideGlance 함수 안에 widget ui를 구성해야 한다.
         *
         * 그러나 그냥 provideGlance 함수 안에서는 Text나 Button 같은 composable function을 사용할 수 없으므로 provideContent를 사용해 그 안에 ui를 구성한다.
         */

        provideContent {
            val count = currentState(key = countKey) ?: 0
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = count.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.White),
                        fontSize = 26.sp
                    )
                )
                Button(
                    text = "추가",
                    onClick = actionRunCallback(IncrementActionCallback::class.java)
                )
            }
        }
    }
}

object IncrementActionCallback : ActionCallback{
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) {
            prefs -> val currentCount = prefs[CounterWidget.countKey]
            if (currentCount != null) prefs[CounterWidget.countKey] = currentCount +1
            else prefs[CounterWidget.countKey] = 1
        }
        CounterWidget.update(context , glanceId)
    }
}

class SimpleCountWidgetReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterWidget

}