# Blueprint Interactor RxJava 3

This library provides building blocks for writing Interactors (use cases as defined in [Clean Architecture][clean-architecture]) based on **RxJava 3**.

## Dependency

```groovy
implementation "io.github.reactivecircus.blueprint:blueprint-interactor-rx3:${blueprint_version}"
```

## Usage

To implement an Interactor we would extend from one of the 3 classes provided:

* `SingleInteractor` for single-shot (with result) tasks
* `CompletableInteractor` for single-shot (no result) tasks
* `ObservableInteractor` for cold streams

Let's say we need an Interactor for fetching a list of users from API. Since there can only be 1 response (or error), our Interactor should extend from `SingleInteractor`:

```kotlin
class FetchUsers(
    private val userService: UserService,
    schedulerProvider: SchedulerProvider
) : SingleInteractor<EmptyParams, List<User>>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: EmptyParams): Single<List<User>> {
        return userService.fetchUsers() // this returns a Single
    }
}
```

To execute this Interactor, build the `Single` and subscribe to it:

```kotlin
disposable += fetchUsers.buildSingle(EmptyParams)
    .subscribeBy(
        onSuccess = { users ->
            // process result
        },
        onError = {
            Timber.e(it)
        }
    )
```

Note that the `SchedulerProvider` in the constructor of the Interactor comes from the [blueprint-async-rx3][async-rx3] artifact, which encapsulates the threading behavior with a wrapper API.

Now let's implement another Interactor for updating a user profile. This interactor expects no result and we just need to know the whether it has been completed successfully. So our Interactor should extend from `CompletableInteractor`:

```kotlin
class UpdateUserProfile(
    private val userService: UserService,
    schedulerProvider: SchedulerProvider
) : CompletableInteractor<UpdateUserProfile.Params>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Completable {
        return userService.updateUserProfile(params.userProfile)
    }
    
    class Params(internal val userProfile: UserProfile) : InteractorParams
}
```

To execute this Interactor, build the `Completable` and subscribe to it:

```kotlin
disposable += updateUserProfile.buildCompletable(UpdateUserProfile.Params(userProfile)).subscribeBy(
        onComplete = {
            Timber.d("Profile updated.")
        },
        onError = {
            Timber.e(it)
        }
    )
```

In a reactive architecture we might want to **stream** any changes to the users persisted in the database to automatically re-render the UI whenever the user list has changed. In this case it makes sense to extend from the `ObservableInteractor`:

```kotlin
class StreamUsers(
    private val userRepository: UserRepository,
    schedulerProvider: SchedulerProvider
) : ObservableInteractor<EmptyParams, List<User>>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Observable<List<User>> {
        return userRepository.streamUsers()
            .map { users ->
                users.sortedBy { it.lastName }
            }
    }
}
```

On the call-side:
 
```kotlin
disposable += streamUsers
    .buildObservable(EmptyParams)
    .subscribeBy(
        onNext = { users ->
            // propagate value of each Observable emission to LiveData<List<User>>
            usersLiveData.value = users
        },
        onError = {
            Timber.e(it)
        }
    )
```

Please check the [Blueprint RxJava Demo app][demo-rx] for more examples of writing and testing Interactors. 

[clean-architecture]: http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
[async-rx3]: ../blueprint-async-rx3/
[demo-rx]: ../samples/demo-rx/
