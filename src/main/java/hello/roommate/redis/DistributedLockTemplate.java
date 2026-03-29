package hello.roommate.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class DistributedLockTemplate {
    private static final int MAX_RETRY = 40;
    private static final long RETRY_DELAY_MS = 15;
    private static final long RETRY_JITTER_MS = 15;

    private final RedisDistributedLockProvider distributedLockProvider;

    public <T> T executeWithLock(String key, long timeoutMs, Supplier<T> loader) {
        for (int i = 0; i < MAX_RETRY; i++) {

            String token = distributedLockProvider.tryLock(key, timeoutMs);

            // 락 가져오기 실패면 재시도
            if (token == null) {
                long jitter = ThreadLocalRandom.current().nextLong(RETRY_JITTER_MS);
                sleep(jitter);
                continue;
            }

            try {
                return loader.get();
            } finally {
                distributedLockProvider.unlock(key, token);
            }
        }
        throw new IllegalStateException("락 획득 실패. 잠시후 다시 시도해주세요.");
    }

    private static void sleep(long jitter) {
        try {
            TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS + jitter);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
