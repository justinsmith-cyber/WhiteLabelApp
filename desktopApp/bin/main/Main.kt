import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.velsol.App
import com.velsol.di.appBrandConfig
import com.velsol.root.DefaultRootComponent
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.SwingUtilities
import kotlin.math.min
import kotlin.math.roundToInt

private fun initialWindowDpSize(): DpSize {
    val s = Toolkit.getDefaultToolkit().screenSize
    val margin = 56
    val availW = (s.width - margin).coerceAtLeast(400)
    val availH = (s.height - margin).coerceAtLeast(300)
    val targetW = (availW * 0.90).roundToInt().coerceAtLeast(min(1024, availW))
    val targetH = (availH * 0.90).roundToInt().coerceAtLeast(min(760, availH))
    return DpSize(targetW.dp, targetH.dp)
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }
    var error: Throwable? = null
    var result: T? = null
    SwingUtilities.invokeAndWait {
        @Suppress("TooGenericExceptionCaught")
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }
    error?.let { throw it }
    @Suppress("UNCHECKED_CAST")
    return result as T
}

fun main() {
    val lifecycle = LifecycleRegistry()
    val root = runOnUiThread {
        DefaultRootComponent(DefaultComponentContext(lifecycle = lifecycle))
    }
    lifecycle.resume()

    application {
        val brandConfig = remember { appBrandConfig() }
        val initialSize = remember { initialWindowDpSize() }
        Window(
            title = brandConfig.appName,
            state = rememberWindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                width = initialSize.width,
                height = initialSize.height,
            ),
            onCloseRequest = {
                lifecycle.stop()
                lifecycle.destroy()
                exitApplication()
            },
        ) {
            window.minimumSize = Dimension(800, 600)
            App(
                rootComponent = root,
                brandConfig = brandConfig,
            )
        }
    }
}
