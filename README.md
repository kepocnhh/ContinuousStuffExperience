[![testing](https://img.shields.io/static/v1?label=testing&message=passed&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/testing/1889a900f09d5085c857c8e7325b88acbd73102317a42a9e1b42b181ac5d65ecea27aae1cf2381198e186825a4a70934f23dff04b08ac5f6a16b51d1b3dfa1b4)
[![test coverage](https://img.shields.io/static/v1?label=test%20coverage&message=100%25&labelColor=212121&color=00c853&style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/reports/coverage/964e5adce7a3c8416ba2fae40b2c7e7f7826c6b4a94d036f03b68cd17309efc487f7b332eb64eae03ce7a14fc88c1a8af9295959130793316735e15c7f60de28)
[![documentation](https://img.shields.io/badge/documentation-2962ff.svg?style=flat)](https://kepocnhh.github.io/ContinuousStuffExperience/documentation/fcd190c523ddc700abab29381e606dbd013eb2a54c36a8a7135b12227da400ca61c1e6034e4bd29d854ec538c7343b5c63d8b5faea5af37974d5367a92378e22)
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