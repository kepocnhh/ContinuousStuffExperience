git clone --depth=1 --branch=gh-pages https://github.com/${TRAVIS_REPO_SLUG}.git ~/gh-pages

git -C ~/gh-pages config user.name "${USER}"
git -C ~/gh-pages config user.email "${USER}"

ls -a ~/gh-pages

sha512=$(sha512sum "${TRAVIS_BUILD_DIR}/build/reports/coverage/testCoverageReport.xml" | cut -d ' ' -f 1)

mkdir -p ~/gh-pages/reports/${sha512}/coverage
mv ${TRAVIS_BUILD_DIR}/build/reports/coverage/* ~/gh-pages/reports/${sha512}/coverage

git -C ~/gh-pages status
git -C ~/gh-pages add --all .
git -C ~/gh-pages commit -m "add ${sha512} test coverage report"
git -C ~/gh-pages push -f -u "https://${git_hub_personal_access_token}@github.com/${TRAVIS_REPO_SLUG}" gh-pages