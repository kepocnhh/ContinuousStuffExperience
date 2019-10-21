[![testing](https://img.shields.io/static/v1?label=testing&message=failed&labelColor=212121&color=d50000&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/testing/bf4a2af794cf2218ff20e9fd22eb3de562c18fe6ee43ccc3117b38b0559d5c9a8e5d672e86f88cad6bcbc656320d044e2d890602df8593bf8f6ea6999d7b85c0/)
[![test coverage](https://img.shields.io/static/v1?label=test%20coverage&message=26%25&labelColor=212121&color=d50000&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/coverage/96cda9f62f1158dfdd75ac9c5a2ea6fc67de6c9bec378927b50d370ff1c9cc9d3358f57ab8c242bd8b9bc37cb24a8af029588fd4c1230db172ea5d281d4937d1/)
[![documentation](https://img.shields.io/badge/documentation-2962ff.svg?style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/documentation/674bb5916a639e92cb8b7cd428a647f4dc32093d2e4562c2378a589e8cf42b5e1511e911baeba743c4e55741eb707324cea886e8e60d061824b5b77ee2af1a33/)
[![code style](https://img.shields.io/static/v1?label=code%20style&message=Kotlin%20Coding%20Conventions&labelColor=212121&color=2962ff&style=flat)](https://kotlinlang.org/docs/reference/coding-conventions.html)

# ContinuousStuffExperience
Testing continuous integration and continuous delivery and / or continuous deployment capabilities.

### Roadmap:

- [x] testing reporting/verification | [JUnit 5](https://junit.org/junit5/)
- [x] test coverage reporting/verification | [JaCoCo](https://www.jacoco.org/jacoco/)
- [x] notifications | [Telegram Bot API](https://core.telegram.org/bots/api/)
- [x] code documentation generate ([dokka](https://github.com/Kotlin/dokka/)) / verification ([detekt](https://github.com/arturbosch/detekt/))
- [x] code style verification | [ktlint](https://ktlint.github.io/)
- [x] check warnings (only kotlin `allWarningsAsErrors = true`)
- [x] automatic merge (only success build of PR to `dev`)
- [ ] check `todo`/`fixme`
- [ ] code analysis
- [ ] check license
- [ ] check/increment version
- [ ] blocking pull/merge requests
- [ ] assembly
- [ ] delivery