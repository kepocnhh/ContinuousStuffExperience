echo "now:"
ls -a
echo "root:"
ls -a /
echo "user:"
ls -a ~
echo "TRAVIS_BUILD_DIR(${TRAVIS_BUILD_DIR}):"
ls -a ~
git clone --depth=1 --branch=gh-pages https://github.com/${TRAVIS_REPO_SLUG}.git ${TRAVIS_BUILD_DIR}/gh-pages
echo "clone: ${TRAVIS_REPO_SLUG}"
ls -a ${TRAVIS_BUILD_DIR}/gh-pages
sha512=$(sha512sum "${TRAVIS_BUILD_DIR}/${TRAVIS_REPO_SLUG}/build/reports/coverage/testCoverageReport.xml" | cut -d ' ' -f 1)
echo "sha512 = ${sha512}"
mkdir -p ${TRAVIS_BUILD_DIR}/gh-pages/reports/${sha512}/coverage
echo "mkdir: ${TRAVIS_BUILD_DIR}/gh-pages/reports/${sha512}/coverage"
mv ${TRAVIS_BUILD_DIR}/${TRAVIS_REPO_SLUG}/build/reports/coverage/* ${TRAVIS_BUILD_DIR}/gh-pages/reports/${sha512}/coverage
echo "move ->"
ls -a ${TRAVIS_BUILD_DIR}/gh-pages/reports/${sha512}/coverage