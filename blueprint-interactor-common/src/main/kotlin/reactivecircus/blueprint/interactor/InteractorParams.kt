package reactivecircus.blueprint.interactor

/**
 * Interface representing params to be passed in for each interactor.
 * Implement this for each interactor that requires specific params.
 */
public interface InteractorParams

/**
 * A special [InteractorParams] representing empty params.
 * Use this when the interactor requires no params.
 */
public object EmptyParams : InteractorParams
