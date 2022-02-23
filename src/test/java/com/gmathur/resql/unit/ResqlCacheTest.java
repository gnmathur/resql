package com.gmathur.resql.unit;

import com.gmathur.resql.Resql;
import com.gmathur.resql.translators.postgres.ResqlWhereProcessorPostgres;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class ResqlCacheTest {
    @Test
    public void testThatCacheHitsHappen() {
        final Resql w = Resql.builder()
                .withWhereBuilder(ResqlWhereProcessorPostgres.class)
                .withCacheExpiryDuration(60L, TimeUnit.MINUTES)
                .withCacheStatsRecorder()
                .build();
        for (int i = 0; i < 123; i++) {
            w.process("f1 > 10");
        }
        assertEquals(122, w.getCacheStats().hitCount());
    }

    @Test
    public void testNoCacheHitsIfAllUniqueKeys() {
        final Resql w = Resql.builder()
                .withWhereBuilder(ResqlWhereProcessorPostgres.class)
                .withCacheExpirySize(1000L)
                .withCacheStatsRecorder()
                .build();
        for (int i = 0; i < 2123; i++) {
            w.process(String.format("f1 > %d", i));
        }
        assertEquals(0, w.getCacheStats().hitCount());
    }

    // TODO It's not the best idea to introduce sleep in tests but we'll let it go for now. Fixing this is a recorded
    // as a TODO item. Also, this might not be a reliable way to test cache expiry
    @Test
    public void testThatCacheEntriesExpire() throws InterruptedException {
        final Resql w = Resql.builder()
                .withWhereBuilder(ResqlWhereProcessorPostgres.class)
                .withCacheExpiryDuration(2L, TimeUnit.SECONDS)
                .withCacheStatsRecorder()
                .build();
        final String input = "f1 > 3579";
        w.process(input);
        Thread.sleep(3000L);
        assertEquals(0, w.getCacheStats().hitCount());
    }
}
