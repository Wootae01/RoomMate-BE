package hello.roommate.redis;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CacheTemplate {

    private final RedisClient redisClient;
    private final DistributedLockTemplate distributedLockTemplate;

    // 캐시 조회 + 분산락
    public <T> T executeWithLock(String cacheKey, Duration ttl, TypeReference<T> typeRef, Supplier<T> loader) {

        // 1. 캐시 조회
        Optional<T> optional = redisClient.get(cacheKey, typeRef);
        if (optional.isPresent()) {
            return optional.get();
        }

        // 2. 캐시 미스면 락 획득 후 double check + DB 조회 + 캐시 저장
        return distributedLockTemplate.executeWithLock(cacheKey, 300, () -> redisClient.get(cacheKey, typeRef).orElseGet(() -> {
            T t = loader.get();
            redisClient.set(cacheKey, t, ttl);
            return t;
        }));
    }

    public <T> T execute(String cacheKey, Duration ttl, TypeReference<T> typeRef, Supplier<T> loader) {
        // 캐시 조회
        Optional<T> optional = redisClient.get(cacheKey, typeRef);
        if (optional.isPresent()) {
            return optional.get();
        }

        // 캐시에 없으면 db에서 조회 후 캐시에 저장
        T t = loader.get();
        redisClient.set(cacheKey, t, ttl);
        return t;
    }

    public void evict(String cacheKey) {
        redisClient.delete(cacheKey);
    }
}
