
git clone --depth=1 --branch=gh-pages https://github.com/${TRAVIS_REPO_SLUG}.git gh-pages/${TRAVIS_REPO_SLUG}

echo "clone: ${TRAVIS_REPO_SLUG}"

ls -a /gh-pages/${TRAVIS_REPO_SLUG}/

sha512=$(sha512sum "/${TRAVIS_REPO_SLUG}/build/reports/coverage/testCoverageReport.xml" | cut -d ' ' -f 1)

echo "sha512 = ${sha512}"

mkdir -p /gh-pages/${TRAVIS_REPO_SLUG}/reports/${sha512}/coverage

echo "mkdir: /gh-pages/${TRAVIS_REPO_SLUG}/reports/${sha512}/coverage"

mv /${TRAVIS_REPO_SLUG}/build/reports/coverage/* /gh-pages/${TRAVIS_REPO_SLUG}/reports/${sha512}/coverage

echo "move ->"

ls -a /gh-pages/${TRAVIS_REPO_SLUG}/reports/${sha512}/coverage