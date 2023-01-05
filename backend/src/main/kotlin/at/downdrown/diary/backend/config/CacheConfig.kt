package at.downdrown.diary.backend.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


const val CACHE_USERS_BY_USERNAME = "Users.byUsername"
const val CACHE_USERS_EXIST_BY_USERNAME = "Users.existByUsername"

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager(
            CACHE_USERS_BY_USERNAME,
            CACHE_USERS_EXIST_BY_USERNAME
        )
    }
}