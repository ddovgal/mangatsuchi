package ua.ddovgal.mangamonitoring.state.util;

import java.util.function.BiFunction;

import ua.ddovgal.mangamonitoring.state.exception.TransitionExecutionException;

/**
 * It's just a {@link BiFunction} that able to throw {@link TransitionExecutionException}.
 *
 * @param <T> the type of the first argument to the function.
 * @param <U> the type of the second argument to the function.
 * @param <R> the type of the result of the function.
 */
@FunctionalInterface
public interface ExecutionFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument.
     * @param u the second function argument.
     *
     * @return the function result.
     */
    R apply(T t, U u) throws TransitionExecutionException;
}
