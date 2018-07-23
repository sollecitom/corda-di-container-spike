# Dependency Injection Container spike

This project is a showcase of how Corda could benefit from a Dependency Injection container.

## What's shown

A Dependency Injection container (Spring Boot) is used to:

- Find and inject types inside the same Corda module.
- Read properties in a cascading way inside the same Corda module.
- Find and inject types from a module's Corda dependencies, thus enabling a plugin-based architecture.
- Find and inject types within a Cordapp module (thus enabling DI in initiated flows).

## Notes and remarks

With regards to what stated above, some remarks:

- Most modules, including all Cordapps, only depend on `javax.inject` and `JSR 250`, meaning they're completely Spring-agnostic.
- Any Dependency Injection container compatible with `javax.inject`, `JSR 250` and package scanning will work.
- The `di-cordapps-resolver` is able to load multiple versions of the same Cordapp at the same time.

## How to run it

- Run `rm -rf ./corda-node/libs/*` and `./gradlew clean build -x test` from within the project directory.
- Run `find . -name "cordapp*all*.jar" | xargs -IjarFile cp -u jarFile ./corda-node/libs`
- Run/Debug `NodeStarter.kt` and watch the console. Then open `Node` and follow the code.
- (After changes to Cordapps) start again from point 1.

## Project modules structure

### corda-node

The active part of the Corda platform. It defines the entry point and, when run, it wires and starts the node.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- spring-boot
- corda
- corda-di-cordapps-resolver (runtime)
- corda-noop-flows-registry (runtime)

### corda

This module defines types similar to those in Corda, which are available for Cordapps. 
For the sake of saving some time, it also has types which should not be available to Cordapps, but in a real scenario it would be trivial 
to migrate them in a separate module.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api

### corda-di-cordapps-resolver

This module provides a Cordapps resolution mechanism based on a Dependency Injection container.
It is used at runtime by `corda-node`.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda

### corda-noop-flows-registry

This module provides a no-op flows registry implementation that simply logs new bindings.
It is used at runtime by `corda-node`.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda

### cordapp-1

This module defines an initiating flow, `QueryClusterAverageTemperature`, along with a domain model.

Relevant dependencies:
- corda

### cordapp-2

This module defines a flow initiated by `QueryClusterAverageTemperature` defined in `cordapp-1`, which uses the Dependency Injection 
Container to inject dependencies inside the flow, and to read properties in a cascading way.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda
- cordapp-1

### cordapp-2v2

This module represents a newer version of module `cordapp-2`. It has the same packages and types, with different behaviour.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda
- cordapp-1

### cordapp-3

This module defines a flow initiated by `QueryClusterAverageTemperature` defined in `cordapp-1`, which uses the Dependency Injection 
Container to inject dependencies inside the flow.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda
- cordapp-1