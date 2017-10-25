# Corda Dependency Injection CordApps Resolver module

This module provides mechanism of resolving CordApps from JARs dropped inside `libs` directory in `corda-node`.
It is able to load multiple versions of the same CordApp at the same time.
Normally, this would be in a separate repository.