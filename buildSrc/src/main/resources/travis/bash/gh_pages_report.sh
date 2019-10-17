LOCAL_PATH="~/gh-pages"

echo "cloning $TRAVIS_REPO_SLUG to $LOCAL_PATH..."

git clone --depth=1 --branch=gh-pages $REPO_URL.git $LOCAL_PATH || exit 1

git -C $LOCAL_PATH config user.name "$USER"
git -C $LOCAL_PATH config user.email "$USER"

testCoverageSignature=$(<"$TRAVIS_BUILD_DIR/build/reports/coverage/signature")
testCoveragePath="$LOCAL_PATH/reports/coverage/$testCoverageSignature"
TEST_COVERAGE_REPORT_URL="$GIT_HUB_PAGES_URL/reports/coverage/$testCoverageSignature"

if [ -d $testCoveragePath ]
then
    echo "test coverage report by $testCoverageSignature already in $LOCAL_PATH"
else
	echo "make dir $testCoveragePath..."
	mkdir -p $testCoveragePath
	echo "move test coverage report"
	mv $TRAVIS_BUILD_DIR/build/reports/coverage/html/* $testCoveragePath

	git -C $LOCAL_PATH add --all .
	git -C $LOCAL_PATH commit -m "add $testCoverageSignature test coverage report"
	git -C $LOCAL_PATH push -f -q -u "https://$git_hub_personal_access_token@github.com/$TRAVIS_REPO_SLUG" gh-pages
fi

testingSignature=$(<"$TRAVIS_BUILD_DIR/build/reports/testing/signature")
testingPath="$LOCAL_PATH/reports/testing/$testingSignature"
TESTING_REPORT_URL="$GIT_HUB_PAGES_URL/reports/testing/$testingSignature"

if [ -d $testingPath ]
then
    echo "test report by $testingSignature already in $LOCAL_PATH"
else
	echo "make dir $testingPath..."
	mkdir -p $testingPath
	echo "move test report"
	mv $TRAVIS_BUILD_DIR/build/reports/testing/html/* $testingPath

	git -C $LOCAL_PATH add --all .
	git -C $LOCAL_PATH commit -m "add $testingSignature test report"
	git -C $LOCAL_PATH push -f -q -u "https://$git_hub_personal_access_token@github.com/$TRAVIS_REPO_SLUG" gh-pages
fi

documentationSignature=$(<"$TRAVIS_BUILD_DIR/build/documentation/signature")
documentationPath="$LOCAL_PATH/documentation/$documentationSignature"
DOCUMENTATION_URL="$GIT_HUB_PAGES_URL/documentation/$documentationSignature"

if [ -d $documentationPath ]
then
    echo "documentation by $documentationSignature already in $LOCAL_PATH"
else
	echo "make dir $documentationPath..."
	mkdir -p $documentationPath
	echo "move documentation"
	mv $TRAVIS_BUILD_DIR/build/documentation/html/* $documentationPath

	git -C $LOCAL_PATH add --all .
	git -C $LOCAL_PATH commit -m "add $testingSignature documentation"
	git -C $LOCAL_PATH push -f -q -u "https://$git_hub_personal_access_token@github.com/$TRAVIS_REPO_SLUG" gh-pages
fi
