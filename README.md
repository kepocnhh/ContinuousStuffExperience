[![testing](https://img.shields.io/static/v1?label=testing&message=passed&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/testing/fc8db3d5e26cee946467c19c2fa72895f6eb7a2c75772db835090b2d259bd455e47395b20687eb426edf97652a96046b46f0a35aa627dbc829f71f90d4b2b0ab/)
[![test coverage](https://img.shields.io/static/v1?label=test%20coverage&message=100%25&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/coverage/98ad10d65852cb3b16bd0477632b87f890c8810233ed2c0ab590f5236734c25305ccd5fe65210304f99957d1842cd1102677dc8d13638426d957e6c3e5783532/)
[![documentation](https://img.shields.io/badge/documentation-2962ff.svg?style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/documentation/253a69f28304bb898f2ce211d23823172f9a49606701118f7e3774f1b7bc59e61d5b35be72da1520bfa15fe423d5174decc89501e323ba4bc5bdd81d77c5ab2d/)
[![code style](https://img.shields.io/static/v1?label=code%20style&message=Kotlin%20Coding%20Conventions&labelColor=212121&color=2962ff&style=flat)](https://kotlinlang.org/docs/reference/coding-conventions.html)
[![current version](https://img.shields.io/static/v1?label=current%20version&message=0.0.5&labelColor=212121&color=2962ff&style=flat)](https://github.com/kepocnhh/ContinuousStuffExperience/releases)

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
- [ ] check/increment version
- [ ] check `todo`/`fixme`
- [ ] code analysis
- [ ] check license
- [ ] blocking pull/merge requests
- [ ] assembly
- [ ] delivery