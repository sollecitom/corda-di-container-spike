# Dependency Injection Container spike

This project is a showcase of how Corda could benefit from a Dependency Injection container.

## What's shown

A Dependency Injection container (Spring Boot) is used to:

- Find and inject types inside the same Corda module.
- Read application properties in a cascading fashion.
- Find and inject types from a module's dependencies.
- Find and inject types from JARs in a specified, thus enabling a plugin-based architecture.
- Find and inject types within a Cordapp module (thus allowing initiated flows to be passed customer defined types through the constructor).
- Make instances of interfaces implemented in Corda available to initiated flows defined in Cordapps (this means the API will never break because of added functionality, without the need to bundle things together).

## Notes and remarks

With regards to what stated above, some remarks:

- Most modules, including all Cordapps, only depend on `javax.inject` and `JSR 250`, meaning they're completely Spring-agnostic.
- Any Dependency Injection container compatible with `javax.inject`, `JSR 250` and package scanning will work.
- The `di-cordapps-resolver` is able to load multiple versions of the same Cordapp at the same time.

## How to run it

- Run `./gradlew clean build -x test` from within the project directory.
- Run/Debug `NodeStarter.kt` and watch the console. Then open `Node` and follow the code.
- (After changes to plugins or Cordapps) start again from point 1.

## Project modules structure

### node

The active part of the Corda platform. It defines the entry point and, when run, it wires and starts the node.

### node-api

This module defines types used by the node to do certain operations. It is imported by the adapters.

### cordapp-api

This module defines types similar to those in Corda, which are available for Cordapps.

### commons

This module defines types that are common to most modules. Ideally this should be replaced by a collection of domain modules, but for the sake of the exercise a common set works fine.

### corda-di-cordapps-resolver

This module provides a Cordapps resolution mechanism based on a Dependency Injection container.
It is used at runtime by `node`.

### corda-noop-flows-registry

This module provides a no-op flows registry implementation that simply logs new bindings.
It produces a JAR plugin that `node` can use.

### cordapp-1

This module defines an initiating flow, `QueryClusterAverageTemperature`, along with a domain model.

### cordapp-2

This module defines a flow initiated by `QueryClusterAverageTemperature` defined in `cordapp-1`, which uses the Dependency Injection Container to inject dependencies inside the flow, and to read properties in a cascading way.

### cordapp-2v2

This module represents a newer version of module `cordapp-2`. It has the same packages and types, with different behaviour.

### cordapp-3

This module defines a flow initiated by `QueryClusterAverageTemperature` defined in `cordapp-1`, which uses the Dependency Injection Container to inject dependencies inside the flow.