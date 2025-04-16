package zahid4kh.qrchitect


import androidx.room.Room
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import zahid4kh.qrchitect.data.AppDatabase
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
        Room.databaseBuilder<AppDatabase>(
            name = AppDatabase.DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().qrCodeDao() }
    single { get<AppDatabase>().templateDao() }

    single { QrRepository(get(), get(), get()) }

    single { QrCodeService() }

    factory { MainViewModel(get(), get()) }
}