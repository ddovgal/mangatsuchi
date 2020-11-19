package ua.ddovgal.mangamonitoring.core.component;

import ua.ddovgal.mangamonitoring.core.ManagedComponent;

/**
 * To be able to interact with users, or in other words, to be able to handle their requests and respond with result, each external system
 * should provide an interlayer between itself and current application by implementing {@link UserInputListener} interface. {@link
 * UserInputListener}s are gateways for all external interactions, representing some kind of mapper or adapter between the external system
 * and this application.
 * <p>
 * To be better understood, for example, if a whole current application would be a web service, {@code UserInputListener} will be an analog
 * of "Representation" (or "Controller") layer, which receives HTTP requests, parses (or maps) them into java objects, invokes necessary
 * service-layer methods and finally possibly responses with result data. By the way, some {@code HttpUserInputListener} implementation will
 * do exactly mentioned in a previous sentence.
 * <p>
 * Due to possible huge differences between implementations and the fact, that all interactions with certain implementation come down to
 * lunching and shutting it down, there are no specific methods that need to be implemented. The only thing, an implementation should do is
 * to map external request to API-layer method call and then present the result of that call.
 * <p>
 * For external systems whose user interaction involves some intermediate steps, {@code ua.ddovgal.manga-monitoring:states} module could be
 * used. It provides the ability to implement stateful interaction. See the key module class {@code StateMachine} for more details.
 */
public interface UserInputListener extends ManagedComponent {}