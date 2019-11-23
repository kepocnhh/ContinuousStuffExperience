[![testing](https://img.shields.io/static/v1?label=testing&message=passed&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/testing/1b22d38381655d9a93fc183c1fab1c47544b95cfebfadc34b977ac1120d92c94f5af64f36886249c75fd3dbec44a2788ad18086164cc21f048ee6f9097c0fcb7/)
[![test coverage](https://img.shields.io/static/v1?label=test%20coverage&message=100%25&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/coverage/24c0f48f19313919903dd50963385775d8913663d29c2ad60cad85fc78357bc02cf94b9711bc3484e06c4964e12ab2ea6f4fcd3aa7f373449e910aec5e6cae80/)
[![documentation](https://img.shields.io/badge/documentation-2962ff.svg?style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/documentation/026832b2f641d7b8b673734f0687c78e7468ae24469dfec85cff177acabc5812390756c2d8a15181daf53c17ee7a136e5d4539ce3ce1e48b4e98d565d7a27191/)
[![code style](https://img.shields.io/static/v1?label=code%20style&message=Kotlin%20Coding%20Conventions&labelColor=212121&color=2962ff&style=flat)](https://kotlinlang.org/docs/reference/coding-conventions.html)
[![current version](https://img.shields.io/static/v1?label=current%20version&message=0.0.7&labelColor=212121&color=2962ff&style=flat)](https://github.com/kepocnhh/ContinuousStuffExperience/releases)

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
- [x] automatic reject PR (only failed build of PR to `dev`)
- [x] check version
- [ ] increment version
- [ ] check `todo`/`fixme`
- [ ] code analysis
- [ ] check license
- [ ] assembly
- [ ] delivery