git clone --depth=1 --branch=gh-pages https://github.com/${TRAVIS_REPO_SLUG}.git ~/gh-pages
echo "clone: ${TRAVIS_REPO_SLUG}"
ls -a ~/gh-pages
sha512=$(sha512sum "${TRAVIS_BUILD_DIR}/build/reports/coverage/testCoverageReport.xml" | cut -d ' ' -f 1)
echo "sha512 = ${sha512}"
mkdir -p ~/gh-pages/reports/${sha512}/coverage
echo "mkdir: ~/gh-pages/reports/${sha512}/coverage"
mv ${TRAVIS_BUILD_DIR}/build/reports/coverage/* ~/gh-pages/reports/${sha512}/coverage
echo "move ->"
ls -a ~/gh-pages/reports/${sha512}/coverage