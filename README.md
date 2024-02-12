
# Mars Rover sample project

A robotic rover is to be landed by NASA on a plateau on Mars. This plateau, which is curiously
rectangular, must be navigated by the rovers so that their on-board cameras can get a complete
view of the surrounding terrain to send back to Earth.

This is a sample project implementing logic to download available instruction sets, create custom ones and processing
them to show the user the result of the applied instructions on the Mars Rover.
The app is developed in Kotlin with full Jetpack Compose ui and MVVM architecture.

## Architecture

To follow clean code principles and avoid dependencies being used where they shouldn't, the app has been divided in 4 
distinct modules:
- **App**: Includes only the main manifest of the app, the MainActivity class and logic to set up dependency injection.
- **Ui**: The ui module is an Android library module that includes all the logic related to the visual side of the app.
This includes the App entrypoint into the compose world, the app screens, navigation logic, viewModels and several related
utilities.
- **Domain**: This pure Kotlin module has all the core business logic to request data to the main repository and process
it to a format that the app can show to the user.
- **Data**: This pure Kotlin module contains the data source of the app and has logic related to handling network connections.

This different modules have dependencies on each other when needed, but always trying to avoid oversharing information.
The dependency graph between them can be seen here:

![App diagram](/images/app-arch-diagram.png)

## Features

The main features found in the app are:

- **Instruction selector**: Shows a list of available instructions for the user to choose. Also has access to the custom
instructions builder.
- **Instruction builder**: From the instruction selector, you can access the instructions builder screen. Here you can 
create your own instructions by configuring all params.
- **Instruction viewer**: From the instruction selector or the instruction builder you can navigate to the instruction 
viewer. This screen will process your instructions and show you the result of the movements or an error if the instructions
con not be recovered or processed.

![App screens diagram](/images/app-screens-diagram.png)

## Libraries used

| Library               | Description                                                                                  |
|-----------------------|----------------------------------------------------------------------------------------------|
| Retrofit              | Abstracts away HTTP connections and working with REST APIs.                                  |
| Kotlinx serialization | This plugin automatically creates serializers and deserializers for the json format.         |
| Arrow                 | Adds functional programming helpers to manage business logic.                                |
| Koin                  | Lightweight dependency injection framework easy to setup and work with.                      |
| Jetpack Compose       | The new official UI framework for Android.                                                   |
| Material 3            | The new material components specification, with new guidelines for colors, shapes and fonts. |
| Navigation compose    | The official way of handling navigation in full Compose apps.                                |
| Compose Lint checks   | A group of lint checks that help creating composable functions without mistakes.             |

## Best practices applied

As small as this app is, it showcases a number of best practices considered the standard for Android development currently.
Some of these practices are:
- **Full Kotlin development**: The app is written 100% in Kotlin, resulting in code more concise and expressive.
- **MVVM architecture**: This architecture is recommended by Google as it offers a clean separation of concerns between
  your ui and business logic.
- **Kotlin's coroutines for async programming**: Coroutines simplify handling async or concurrent operations and make the
  code more readable and testable.
- **Follows Material Design guidelines**: Using standard design principles makes easier for the user to understand how to
  navigate your app and makes sure it is accessible for most people.
- **Dependency injection**: Dependency injection frameworks help separate concerns, reuse instances of components and makes
  it easy to replace components for testing.
- **Testing**: Test help catch code issues early and improve its maintainability and reusability. In this app, 95% of the 
business logic is unit tested and 60% of the ui logic is covered by ui tests.