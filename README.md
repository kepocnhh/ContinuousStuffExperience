[![testing](https://img.shields.io/static/v1?label=testing&message=passed&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/testing/efec849b57e2c9fa65d06405b15fdc5032c4578077761327819251db0feec12f80a9c9cf65ecffb163b6e3acf65be48fbc851fa0b05a8ea5ba615d413f910add/)
[![test coverage](https://img.shields.io/static/v1?label=test%20coverage&message=77%25&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/coverage/ab09e320c37bb9d52bb52821aba89a9f2a271b6e30b62cedb48c6997fe91f9dc4588f5baf220350a47330de51b864ecb1095f2344732e3b8af50d5addc9d17ff/)
[![documentation](https://img.shields.io/badge/documentation-2962ff.svg?style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/documentation/fcd190c523ddc700abab29381e606dbd013eb2a54c36a8a7135b12227da400ca61c1e6034e4bd29d854ec538c7343b5c63d8b5faea5af37974d5367a92378e22/)
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