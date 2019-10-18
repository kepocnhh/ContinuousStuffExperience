[![testing](https://img.shields.io/static/v1?label=testing&message=passed&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/testing/36d54b7eaa6ad93462aae74ad3aff3096f05b2e244adef1fb17e9c77b45c1d45cb646ed511928e66397c0b4fe57b709135189a471157e9051acebe88aa0fc3e4/)
[![test coverage](https://img.shields.io/static/v1?label=test%20coverage&message=44%25&labelColor=212121&color=d50000&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/coverage/d254736b2c53d94a12c5431c037e2505926e9813401bc13d2dfa58ec93aa2543b181975ebfe5f919d536d77cc4ec49f3253e8142449bd6568e3feae146ea8116/)
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