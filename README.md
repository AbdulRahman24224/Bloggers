# Bloggers
A sample blogging platform app that represents a showcase of applying Clean Architecture and practicing the newest libraries in android .

## Project Architecture 
The App Follows Clean Architecture for what it brings in of Modularity , Reusability and Dependencies Direction insurance .
     The projects is Divided into 3 Modules(Layer) from bottom up  [ Entites(Data)  ,  Domian  , Presentation  ] 

1. Entities Layer : This ayer contains Data Models and data sources used in the project .
2. Domain Layer : This layer is dependant on the entities and it contains all app domain (business) logic , it uses repositories that has data sources injected to it to implement some usecase which should be pure functions that take needed parmatets and returns data or state after applying the usecase logic .  
3. Presentation Layer : Implementing Unidirectional Data Flow for UI using MVI because it is most suitable to use with comopose where view fires intents  through SharedFlow<Intent> to viewmodel which invokes suitable usecase and reduced the changes to a single state which is observed and drawn by view as StateFlow<T>

### Stack 
* Compose : Building richier and faster ui , benefit from kotlin apis in view writing .
* Coroutines and Flow : Flow has relatively easier api than RxJava , we also benefit from scopes especially viewmodel scope which automatically cancel all launched jobs for us and help reduce memory leaks  , also the support from compose to collect flow as state is helpful too .
* Dagger Hilt : Easier creation and location of dependencies , viewmodel injecting and creation .
* Room : For data caching as it is suitable for large data storing and also supports coroutines .

### Todos 
- Tests
- ui features into separate modules
- Supportin Multiple themes [WIP]
- finish enhancments todos
