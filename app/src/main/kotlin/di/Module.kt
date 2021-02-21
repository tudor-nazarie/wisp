package di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

inline fun <reified T> getInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

val wispModule = module {
    single { provideMoshi() }

    single { provideOpenDotaService(get()) }

    single { provideDateTimeFormatter() }
}
