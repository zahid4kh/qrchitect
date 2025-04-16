package zahid4kh.qrchitect

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import zahid4kh.qrchitect.data.AppDatabaseHelper
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
        val dbHelper = AppDatabaseHelper()
        dbHelper.initialize("qrchitect.db")
    }

    single { QrRepository(get(), get()) }

    single { QrCodeService() }

    factory { MainViewModel(get(), get()) }
}