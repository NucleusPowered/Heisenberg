Nucleus Heisenberg
====

* [Source]
* [Issues]
* [Discord]

Licence: [MIT](LICENSE.md) (except for the Nucleus logo, which is all rights reserved)

The GeoIP module from Nucleus now on its own.

Why Heisenberg? The [Heisenberg Uncertainty Principle](https://en.wikipedia.org/wiki/Uncertainty_principle) states that you 
cannot know both someone's position and momentum at the same time. We'll know their position! 

## Getting and Building Heisenberg

To get a copy of the Heisenberg source, ensure you have Git installed, and run the following commands from a command prompt
or terminal:

1. `git clone git@github.com:NucleusPowered/Heisenberg.git`
2. `cd Heisenberg`
3. `cp scripts/pre-commit .git/hooks`

To build Heisenberg, navigate to the source directory and run either:

* `./gradlew build` on UNIX and UNIX like systems (including macOS and Linux)
* `gradlew build` on Windows systems

You will find the compiled JAR which will be named like `Heisenberg-[version].jar` in `output/`.

## Third Party Libraries

The compiled Heisenberg plugin includes the following libraries (with their licences in parentheses):

* MaxMind GeoIP2 API (Apache 2)
* MaxMind DB (Apache 2)
* Jackson (Apache 2)

See [THIRDPARTY.md](THIRDPARTY.md) for more details.

[Source]: https://github.com/NucleusPowered/Heisenberg
[Issues]: https://github.com/NucleusPowered/Heisenberg/issues
[Discord]: https://discord.gg/A9QHG5H
