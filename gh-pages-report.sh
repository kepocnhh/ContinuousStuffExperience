echo "cloning ${TRAVIS_REPO_SLUG} to ~/gh-pages..."
git clone --depth=1 --branch=gh-pages https://github.com/${TRAVIS_REPO_SLUG}.git ~/gh-pages || exit 1

git -C ~/gh-pages config user.name "${USER}"
git -C ~/gh-pages config user.email "${USER}"

testCoverageSignature=$(<"${TRAVIS_BUILD_DIR}/build/reports/coverage/signature")
testCoveragePath="$HOME/gh-pages/reports/coverage/${testCoverageSignature}"

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

testSignature=$(<"${TRAVIS_BUILD_DIR}/build/reports/tests/signature")
testPath="$HOME/gh-pages/reports/tests/${testSignature}"

if [ -d $testPath ]
then
    echo "test report by ${testSignature} already in gh-pages"
else
	echo "make dir $testPath..."
	mkdir -p $testPath
	echo "move test report"
	mv ${TRAVIS_BUILD_DIR}/build/reports/tests/html/* $testPath

	git -C ~/gh-pages add --all .
	git -C ~/gh-pages commit -m "add ${testSignature} test report"
	git -C ~/gh-pages push -f -q -u "https://${git_hub_personal_access_token}@github.com/${TRAVIS_REPO_SLUG}" gh-pages
fi
