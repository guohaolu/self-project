package org.example.pool;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池的{@link ThreadFactory}
 */
public class UserThreadFactory implements ThreadFactory {
    private final String namePrefix;

    private final AtomicInteger nextId = new AtomicInteger(1);

    UserThreadFactory(String whatFeatureOfGroup) {
        namePrefix = "USER THREAD FACTORY " + whatFeatureOfGroup + "-Worker-";
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param task a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(@Nonnull Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        Thread thread = new Thread(null, task, name);
        System.out.println(thread.getName());
        return thread;
    }
}
