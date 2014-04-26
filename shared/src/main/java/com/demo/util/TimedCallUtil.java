package com.demo.util;

import com.demo.core.Command;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

@Service("timedCallUtil")
public class TimedCallUtil {

    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    public <T> T call(FutureTask<T> task, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {

        Validate.notNull(task, "Task must not be null");

        THREAD_POOL.execute(task);
        return task.get(timeout, timeUnit);
    }

    public <T> T call(final Command<T> command, long maxTimeToWait, TimeUnit maxTimeToWaitUnit, long interval, TimeUnit intervalUnit) throws InterruptedException, ExecutionException, TimeoutException {

        Validate.notNull(command, "Command must not be null");

        final long intervalMillis = TimeUnit.MILLISECONDS.convert(interval, intervalUnit);

        T result = null;
        FutureTask<T> task = null;
        try {
            task = new FutureTask<T>(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    T result = null;
                    while (!Thread.interrupted()) {
                        result = command.execute();

                        if (result != null) {
                            break;
                        }

                        Thread.currentThread().sleep(intervalMillis);
                    }
                    return result;
                }
            });
            result = call(task, maxTimeToWait, maxTimeToWaitUnit);
        } finally {
            if (task != null) {
                try {
                    task.cancel(true);
                } catch (Exception e) {
                }
            }
        }

        return result;
    }
}
