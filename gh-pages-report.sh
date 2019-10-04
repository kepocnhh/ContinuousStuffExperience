echo "cloning ${TRAVIS_REPO_SLUG} to ~/gh-pages..."
git clone --depth=1 --branch=gh-pages ${REPO_URL}.git ~/gh-pages || exit 1

git -C ~/gh-pages config user.name "${USER}"
git -C ~/gh-pages config user.email "${USER}"

testCoverageSignature=$(<"${TRAVIS_BUILD_DIR}/build/reports/coverage/signature")
testCoveragePath="$HOME/gh-pages/reports/coverage/${testCoverageSignature}"
TEST_COVERAGE_REPORT_URL="$GIT_HUB_PAGES_URL/reports/coverage/${testCoverageSignature}"

if [ -d $testCoveragePath ]
then
    echo "test coverage report by ${testCoverageSignature} already in gh-pages"
else
	echo "make dir $testCoveragePath..."
	mkdir -p $testCoveragePath
	echo "move test coverage report"
	mv ${TRAVIS_BUILD_DIR}/build/reports/coverage/html/* $testCoveragePath

	git -C ~/gh-pages add --all .
	git -C ~/gh-pages commit -m "add ${testCoverageSignature} test coverage report"
	git -C ~/gh-pages push -f -q -u "https://${git_hub_personal_access_token}@github.com/${TRAVIS_REPO_SLUG}" gh-pages
fi

testingSignature=$(<"${TRAVIS_BUILD_DIR}/build/reports/testing/signature")
testingPath="$HOME/gh-pages/reports/testing/${testingSignature}"
TESTING_REPORT_URL="$GIT_HUB_PAGES_URL/reports/testing/${testingSignature}"

if [ -d $testingPath ]
then
    echo "test report by ${testingSignature} already in gh-pages"
else
	echo "make dir $testingPath..."
	mkdir -p $testingPath
	echo "move test report"
	mv ${TRAVIS_BUILD_DIR}/build/reports/testing/html/* $testingPath

	git -C ~/gh-pages add --all .
	git -C ~/gh-pages commit -m "add ${testingSignature} test report"
	git -C ~/gh-pages push -f -q -u "https://${git_hub_personal_access_token}@github.com/${TRAVIS_REPO_SLUG}" gh-pages
fi

documentationSignature=$(<"${TRAVIS_BUILD_DIR}/build/documentation/signature")
documentationPath="$HOME/gh-pages/documentation/${documentationSignature}"
DOCUMENTATION_URL="$GIT_HUB_PAGES_URL/documentation/${documentationSignature}"

if [ -d $documentationPath ]
then
    echo "documentation by ${documentationSignature} already in gh-pages"
else
	echo "make dir $documentationPath..."
	mkdir -p $documentationPath
	echo "move documentation"
	mv ${TRAVIS_BUILD_DIR}/build/documentation/html/* $documentationPath

	git -C ~/gh-pages add --all .
	git -C ~/gh-pages commit -m "add ${testingSignature} documentation"
	git -C ~/gh-pages push -f -q -u "https://${git_hub_personal_access_token}@github.com/${TRAVIS_REPO_SLUG}" gh-pages
fi
