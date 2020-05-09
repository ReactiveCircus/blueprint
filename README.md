# Blueprint

[![CircleCI](https://circleci.com/gh/ReactiveCircus/blueprint.svg?style=svg)](https://circleci.com/gh/ReactiveCircus/blueprint)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.reactivecircus.blueprint/blueprint-ui/badge.svg)](https://search.maven.org/search?q=g:io.github.reactivecircus.blueprint)
[![Android API](https://img.shields.io/badge/API-21%2B-blue.svg?label=API&maxAge=300)](https://www.android.com/history/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Architectural frameworks and toolkits for bootstrapping modern Android codebases, written in **Kotlin**.

## Motivation

These days whenever I kickoff a new Android project I always start by adding a bunch of infrastructure code (frameworks and toolkits) I developed over the years as they work really well in modern Android codebases which follow **Clean Architecture** and **reactive paradigm**, with strong emphasis on **automated UI testing**.

These frameworks and toolkits have now become a library.

## Dependencies

**Blueprint** offers multiple independent artifacts covering different use cases. But they are all "optional" which means you can choose to only use the ones that make sense for your codebase.

Dependencies are hosted on [Maven Central][maven-central].

Latest stable version:

```groovy
def blueprint_version = "1.8.0"
```

If you use **Kotlin Coroutines and Flow**:

```groovy
// Building Interactors based on Kotlin Coroutines and Flow
implementation "io.github.reactivecircus.blueprint:blueprint-interactor-coroutines:${blueprint_version}"

// Wrapper API for doing async work with Kotlin CoroutineDispatcher
implementation "io.github.reactivecircus.blueprint:blueprint-async-coroutines:${blueprint_version}"

// Android UI extensions, utilities and widgets
implementation "io.github.reactivecircus.blueprint:blueprint-ui:${blueprint_version}"

// Android UI testing framework with Testing Robot DSL
implementation "io.github.reactivecircus.blueprint:blueprint-testing-robot:${blueprint_version}"
```

If you use **RxJava 2**:

```groovy
// Building Interactors based on RxJava 2
implementation "io.github.reactivecircus.blueprint:blueprint-interactor-rx2:${blueprint_version}"

// Wrapper API for doing async work with RxJava 2 Schedulers
implementation "io.github.reactivecircus.blueprint:blueprint-async-rx2:${blueprint_version}"

// Android UI extensions, utilities and widgets
implementation "io.github.reactivecircus.blueprint:blueprint-ui:${blueprint_version}"

// Android UI testing framework with Testing Robot DSL
implementation "io.github.reactivecircus.blueprint:blueprint-testing-robot:${blueprint_version}"
```

If you use **RxJava 3**:

```groovy
// Building Interactors based on RxJava 3
implementation "io.github.reactivecircus.blueprint:blueprint-interactor-rx3:${blueprint_version}"

// Wrapper API for doing async work with RxJava 3 Schedulers
implementation "io.github.reactivecircus.blueprint:blueprint-async-rx3:${blueprint_version}"

// Android UI extensions, utilities and widgets
implementation "io.github.reactivecircus.blueprint:blueprint-ui:${blueprint_version}"

// Android UI testing framework with Testing Robot DSL
implementation "io.github.reactivecircus.blueprint:blueprint-testing-robot:${blueprint_version}"
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

## Samples

There are a couple of [sample apps][samples] demonstrating how you may use **Blueprint**, one based on **Kotlin Coroutines** and the other one based on **RxJava 2**.

## Overview

The following is a high-level overview of what each **Blueprint** artifact does. For details and sample usage please check the [samples][samples] and the documentation (**README.md**) in each of the sub-projects.

### Blueprint Interactor Coroutines

This artifact provides 2 base classes for building Interactors (use case in Clean Architecture):

* `SuspendingInteractor` for single-shot tasks
* `FlowInteractor` for cold streams 

Please check [blueprint-interactor-coroutines][interactor-coroutines] for details.

### Blueprint Async Coroutines

This artifact provides a `CoroutineDispatcherProvider` API for encapsulating the threading behavior with Kotlin CoroutineDispatcher.

Please check [blueprint-async-coroutines][async-coroutines] for details.

### Blueprint Interactor RxJava 2

This artifact provides 3 base classes for building Interactors (use case in Clean Architecture):

* `SingleInteractor` for single-shot (with result) tasks
* `CompletableInteractor` for single-shot (no result) tasks
* `ObservableInteractor` for cold streams

Please check [blueprint-interactor-rx2][interactor-rx2] for details.

### Blueprint Async RxJava 2

This artifact provides a `SchedulerProvider` API for encapsulating the threading behavior with RxJava 2 Schedulers.

Please check [blueprint-async-rx2][async-rx2] for details.

### Blueprint Interactor RxJava 3

This artifact provides 3 base classes for building Interactors (use case in Clean Architecture):

* `SingleInteractor` for single-shot (with result) tasks
* `CompletableInteractor` for single-shot (no result) tasks
* `ObservableInteractor` for cold streams

Please check [blueprint-interactor-rx3][interactor-rx3] for details.

### Blueprint Async RxJava 3

This artifact provides a `SchedulerProvider` API for encapsulating the threading behavior with RxJava 3 Schedulers.

Please check [blueprint-async-rx3][async-rx3] for details.

### Blueprint UI

This artifact provides convenient Kotlin extensions and widgets for working with the Android UI toolkit.

Please check [blueprint-ui][ui] for details.

### Blueprint Testing Robot

This artifact provides a UI testing framework and [Testing Robot][testing-robot-article] DSL for authoring structured, readable, and framework-agnostic UI tests.

Please check [blueprint-testing-robot][testing-robot] for details.

## License

```
Copyright 2019 Yang Chen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[maven-central]: https://search.maven.org/search?q=g:io.github.reactivecircus.blueprint
[snap]: https://oss.sonatype.org/content/repositories/snapshots/
[samples]: samples/
[interactor-coroutines]: blueprint-interactor-coroutines/
[async-coroutines]: blueprint-async-coroutines/
[interactor-rx2]: blueprint-interactor-rx2/
[async-rx2]: blueprint-async-rx2/
[interactor-rx3]: blueprint-interactor-rx3/
[async-rx3]: blueprint-async-rx3/
[ui]: blueprint-ui/
[testing-robot]: blueprint-testing-robot/
[testing-robot-article]: https://academy.realm.io/posts/kau-jake-wharton-testing-robots/
