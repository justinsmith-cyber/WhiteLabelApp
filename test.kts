import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

fun test(options: KotlinJvmCompilerOptions) {
    options.allWarningsAsErrors.set(true)
}
