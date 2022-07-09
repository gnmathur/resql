package com.gmathur.resql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.gmathur.resql.exceptions.DefaultResqlExceptionHandler;
import com.gmathur.resql.exceptions.ResqlExceptionHandler;
import com.gmathur.resql.translators.ResqlWhereProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * Resql context - users create the context specifying the exception handler, and the database where clause processor
 *
 * @author Gaurav Mathur (gnmathur)
 */
public class Resql {//
    /// Class
    private final ResqlWhereProcessor resqlWhereProcessor;
    private Cache<Integer, String> cache = null;

    Resql(final ResqlWhereProcessor resqlWhereProcessor, Cache<Integer, String> cache) {
        this.resqlWhereProcessor = resqlWhereProcessor;
        this.cache = cache;
    }

    public ResqlWhereProcessor getResqlWhereProcessor() {
        return resqlWhereProcessor;
    }

    public String process(final String resqlWhereClause) {
        return resqlWhereProcessor.processInput(resqlWhereClause);
    }

    public CacheStats getCacheStats() {
        return cache.stats();
    }

    public Long getEstimatedCacheSize() {
        return cache.estimatedSize();
    }

    public static ResqlBuilder builder() {
        return new ResqlBuilder();
    }

    /// Builder

    public static class ResqlBuilder {
        private ResqlExceptionHandler exception;
        private Class<? extends ResqlWhereProcessor> resqlWhereProcessor = null;
        private Long cacheExpiryDuration = null;
        private TimeUnit cacheExpiryDurationTimeUnit = null;
        private Long maxCacheSize = null;
        private Boolean recordCacheStats = false;

        private ResqlBuilder() {
            exception = new DefaultResqlExceptionHandler();
        }

        public ResqlBuilder withExceptionHandler(final ResqlExceptionHandler exceptionHandler) {
            this.exception = exceptionHandler;
            return this;
        }

        public ResqlBuilder withWhereBuilder(final Class<? extends ResqlWhereProcessor> resqlWhereProcessor) {
            this.resqlWhereProcessor = resqlWhereProcessor;
            return this;
        }

        public ResqlBuilder withCacheExpiryDuration(Long duration, TimeUnit timeUnit) {
            this.cacheExpiryDuration = duration;
            this.cacheExpiryDurationTimeUnit = timeUnit;
            return this;
        }

        public ResqlBuilder withCacheStatsRecorder() {
            this.recordCacheStats = true;
            return this;
        }

        public ResqlBuilder withCacheExpirySize(Long maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public Resql build() {
            if (resqlWhereProcessor == null) {
                throw new RuntimeException("resql needs a WHERE builder for the target database");
            }
            if (cacheExpiryDurationTimeUnit != null && cacheExpiryDuration == null) {
                throw new RuntimeException("resql cache expiry time duration unit specified without value");
            }
            if (cacheExpiryDuration != null && cacheExpiryDurationTimeUnit == null) {
                throw new RuntimeException("resql cache expiry time value specified without time duration unit");
            }
            if (recordCacheStats && (cacheExpiryDuration == null && maxCacheSize == null)) {
                throw new RuntimeException("resql cannot record cache stats as cache is not configured");
            }

            try {
                Cache<Integer, String> cache = null;
                if (cacheExpiryDuration != null || maxCacheSize != null) {
                    Caffeine<Object, Object> c = Caffeine.newBuilder();
                    if (cacheExpiryDuration != null) {
                        c.expireAfterWrite(cacheExpiryDuration, cacheExpiryDurationTimeUnit);
                    }
                    if (maxCacheSize != null) {
                        c.maximumSize(maxCacheSize);
                    }
                    if (recordCacheStats) {
                        c.recordStats();
                    }
                    cache = c.build();
                }
                if (cache != null) {
                    Class[] clsArgs = new Class[2];
                    clsArgs[0] = ResqlExceptionHandler.class;
                    clsArgs[1] = Cache.class;
                    return new Resql(resqlWhereProcessor
                            .getDeclaredConstructor(clsArgs)
                            .newInstance(exception, cache), cache);
                } else {
                    Class[] clsArgs = new Class[1];
                    clsArgs[0] = ResqlExceptionHandler.class;
                    return new Resql(resqlWhereProcessor
                            .getDeclaredConstructor(clsArgs)
                            .newInstance(exception), null);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Error creating resql instance (err: " + e.getMessage() + ")");
            }
        }
    }
}
