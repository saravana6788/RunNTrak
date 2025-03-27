import com.skcodes.core.domain.run.LocalRunDataSource
import com.skcodes.core.domain.run.RemoteRunDataSource
import com.skcodes.run.network.KtorRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkKoinModule = module{

    singleOf(::KtorRunDataSource).bind<RemoteRunDataSource>()
}
