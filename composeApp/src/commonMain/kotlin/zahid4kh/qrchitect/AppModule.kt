package zahid4kh.qrchitect

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import zahid4kh.qrchitect.data.QrDatabase
import zahid4kh.qrchitect.data.QrRepository
import zahid4kh.qrchitect.domain.QrCodeService
import zahid4kh.qrchitect.ui.MainViewModel

val appModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        }
    }

    single {
        QrDatabase().initialize()
    }

    single { QrRepository(get(), get()) }

    single { QrCodeService() }

    factory { MainViewModel(get(), get()) }
}