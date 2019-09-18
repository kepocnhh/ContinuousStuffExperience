git clone --depth=1 --branch=gh-pages https://github.com/${TRAVIS_REPO_SLUG}.git ~/gh-pages

git -C ~/gh-pages config user.name "${USER}"
git -C ~/gh-pages config user.email "${USER}"

signature=$(<"${TRAVIS_BUILD_DIR}/build/reports/coverage/signature")

mkdir -p ~/gh-pages/reports/${signature}/coverage
mv ${TRAVIS_BUILD_DIR}/build/reports/coverage/* ~/gh-pages/reports/${signature}/coverage

git -C ~/gh-pages status
git -C ~/gh-pages add --all .
git -C ~/gh-pages commit -m "add ${signature} test coverage report"
git -C ~/gh-pages push -f -q -u "https://${git_hub_personal_access_token}@github.com/${TRAVIS_REPO_SLUG}" gh-pages