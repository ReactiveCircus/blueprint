# Blueprint Interactor Coroutines

This library provides building blocks for writing Interactors (use cases as defined in [Clean Architecture][clean-architecture]) based on **Kotlin Coroutines and Flow**.

## Dependency

```groovy
implementation "io.github.reactivecircus.blueprint:blueprint-interactor-coroutines:${blueprint_version}"
```

## Usage

To implement an Interactor we would extend from one of the 2 classes provided:

* `SuspendingInteractor` for single-shot tasks
* `FlowInteractor` for cold streams

Let's say we need an Interactor for fetching a list of users from API. Since there can only be 1 response (or error), our Interactor should extend from `SuspendingInteractor`:

```kotlin
class FetchUsers(
    private val userService: UserService,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<EmptyParams, List<User>>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override suspend fun doWork(params: EmptyParams): List<User> {
        userService.fetchUsers()
    }
}
```

To execute this Interactor, call the `execute(...)` suspend function from a `CoroutineScope`:

```kotlin
viewModelScope.launch {
    // EmptyParams is a special [InteractorParams] which you can use when the Interactor has no params.
    val users: List<User> = fetchUsers.execute(EmptyParams)
}
```

Note that the `CoroutineDispatcherProvider` in the constructor of the Interactor comes from the [blueprint-async-coroutines][async-coroutines] artifact, which encapsulates the threading behavior with a wrapper API.

Now let's implement another Interactor for updating a user profile. This interactor expects no result and we just need to know the whether it has been completed successfully. So our Interactor should again extend from `SuspendingInteractor`:

```kotlin
class UpdateUserProfile(
    private val userService: UserService,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<UpdateUserProfile.Params, Unit>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override suspend fun doWork(params: Params) {
        userService.updateUserProfile(params.userProfile)
    }

    class Params(internal val userProfile: UserProfile) : InteractorParams
}
```

To execute this Interactor, call the `execute(...)` suspend function from a `CoroutineScope`:

```kotlin
viewModelScope.launch {
    // this returns a Unit
    updateUserProfile.execute(UpdateUserProfile.Params(userProfile))
}
```

In a reactive architecture we might want to **stream** any changes to the users persisted in the database to automatically re-render the UI whenever the user list has changed. In this case it makes sense to extend from the `FlowInteractor`:

```kotlin
class StreamUsers(
    private val userRepository: UserRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : FlowInteractor<EmptyParams, List<User>>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override fun createFlow(params: Params): Flow<List<User>> {
        return userRepository.streamUsers() // this returns a Coroutines Flow
            .map { users ->
                users.sortedBy { it.lastName }
            }
    }
}
```

On the call-side:
 
```kotlin
streamUsers.buildFlow(EmptyParams)
    .onEach { users ->
        // propagate value of each Flow emission to LiveData<List<User>>
        usersLiveData.value = users
    }
    .catch {
        Timber.e(it)
    }
    // launch the collection of the Flow in the [viewModelScope] from "androidx.lifecycle:lifecycle-viewmodel-ktx"
    .launchIn(viewModelScope)
```

Please check the [Blueprint Coroutines Demo app][demo-coroutines] for more examples of writing and testing Interactors. 

[clean-architecture]: http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
[async-coroutines]: ../blueprint-async-coroutines/
[demo-coroutines]: ../samples/demo-coroutines/
