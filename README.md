#Dependency Injection Container spike

This project is a showcase of how Corda could benefit from a Dependency Injection container.

##What's shown

A Dependency Injection container (Spring Boot) is used to:

- Find and inject types inside the same Corda module.
- Read properties in a cascading way inside the same Corda module.
- Find and inject types from a module's Corda dependencies, thus enabling a plugin-based architecture.
- Read properties in a cascading way from a module's Corda dependencies (not shown in this case but shown for CordApps modules).
- Find and inject types in a Corda module from non-Corda modules (thus enabling CordApps loading).
- Find and inject types within a CordApp module (thus enabling DI in initiated flows).
- Read properties in a cascading way within a CordApp module (thus enabling CordApps to specify properties in a convenient way).

##Notes and remarks

With regards to what stated above, some remarks:

- Most modules, including some CordApps only depend on `javax.inject` and `JSR 250`, meaning they're completely Spring-agnostic.
- Spring provides a superset of the features offered by `javax.inject` and `JSR 250`, meaning modules that want to leverage those (property values injection, etc.) depend on `spring-context` (just annotations) but still are agnostic of `spring-boot`.
- If we decide to use `Spring Boot` as a Dependency Injection Container we enable CordApps to leverage a wider set of feature.
- The entire project took roughly 3 hours, including documentation. It's written in Kotlin and works with both JDK8 and JDK9.

##How to run it

Run/Debug `NodeStarter.kt` and watch the console. Then open `Node` and follow the code.

##Project modules structure

###corda-node

The active part of the Corda platform. It defines the entry point and, when run, it wires and starts the node.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- spring-boot
- corda
- corda-di-cordapps-resolver (runtime)
- corda-noop-flows-registry (runtime)
- cordapp-1
- cordapp-2
- cordapp-3

###corda

This module defines types similar to those in Corda, which are available for CordApps. 
For the sake of saving some time, it also has types which should not be available to CordApps, but in a real scenario it would be trivial 
to migrate them in a separate module.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api

###cora-di-cordapps-resolver

This module provides a CordApps resolution mechanism based on a Dependency Injection container.
It is used at runtime by `corda-node`.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda

###cora-noop-flows-registry

This module provides a no-op flows registry implementation that simply logs new bindings.
It is used at runtime by `corda-node`.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda

###cordapp-1

This module defines an initiating flow, `QueryClusterAverageTemperature`, along with a domain model.

Relevant dependencies:
- corda

###cordapp-2

This module defines a flow initiated by `QueryClusterAverageTemperature` defined in `cordapp-1`, which uses the Dependency Injection 
Container to inject dependencies inside the flow, and to read properties in a cascading way.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda
- org.springframework:spring-context
- cordapp-1

###cordapp-3

This module defines a flow initiated by `QueryClusterAverageTemperature` defined in `cordapp-1`, which uses the Dependency Injection 
Container to inject dependencies inside the flow.

Relevant dependencies:
- javax.inject:javax.inject
- javax.annotation:jsr250-api
- corda
- cordapp-1