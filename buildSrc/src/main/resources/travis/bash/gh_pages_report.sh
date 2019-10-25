LOCAL_PATH="~/gh-pages"

GIT_URL="https://$git_hub_personal_access_token@github.com/$REPO_SLUG"

#__________ __________ cloning >

echo $newline
echo "cloning $REPO_SLUG to $LOCAL_PATH..."
git clone -q --depth=1 --branch=gh-pages $GIT_URL.git $LOCAL_PATH || exit 1

git -C $LOCAL_PATH config user.name "$USER"
git -C $LOCAL_PATH config user.email "$USER"

#__________ __________ test coverage >

testCoverageSignature=$(<"$BUILD_DIR/build/reports/coverage/signature")
testCoveragePath="$LOCAL_PATH/reports/coverage/$testCoverageSignature"
TEST_COVERAGE_REPORT_URL="$GIT_HUB_PAGES_URL/reports/coverage/$testCoverageSignature/"

ILLEGAL_STATE=0

if [ -d $testCoveragePath ]
then
  echo $newline
  echo "test coverage report by $testCoverageSignature already in $LOCAL_PATH"
else
  echo $newline
	echo "make dir $testCoveragePath..."
	mkdir -p $testCoveragePath
	echo "move test coverage report"
	mv $BUILD_DIR/build/reports/coverage/html/* $testCoveragePath

  echo $newline
	git -C $LOCAL_PATH add --all . || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "adding failed!"
    exit $ILLEGAL_STATE
  fi
	git -C $LOCAL_PATH commit -m "add $testCoverageSignature test coverage report" || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "commiting failed!"
    exit $ILLEGAL_STATE
  fi
	git -C $LOCAL_PATH push -f -q || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "pushing failed!"
    exit $ILLEGAL_STATE
  fi
fi

#__________ __________ testing >

testingSignature=$(<"$BUILD_DIR/build/reports/testing/signature")
testingPath="$LOCAL_PATH/reports/testing/$testingSignature"
TESTING_REPORT_URL="$GIT_HUB_PAGES_URL/reports/testing/$testingSignature/"

if [ -d $testingPath ]
then
  echo $newline
  echo "test report by $testingSignature already in $LOCAL_PATH"
else
  echo $newline
	echo "make dir $testingPath..."
	mkdir -p $testingPath
	echo "move test report"
	mv $BUILD_DIR/build/reports/testing/html/* $testingPath

  echo $newline
	git -C $LOCAL_PATH add --all . || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "adding failed!"
    exit $ILLEGAL_STATE
  fi
	git -C $LOCAL_PATH commit -m "add $testingSignature test report" || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "commiting failed!"
    exit $ILLEGAL_STATE
  fi
	git -C $LOCAL_PATH push -f -q || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "pushing failed!"
    exit $ILLEGAL_STATE
  fi
fi

#__________ __________ documentation >

documentationSignature=$(<"$BUILD_DIR/build/documentation/signature")
documentationPath="$LOCAL_PATH/documentation/$documentationSignature"
DOCUMENTATION_URL="$GIT_HUB_PAGES_URL/documentation/$documentationSignature/"

if [ -d $documentationPath ]
then
  echo $newline
  echo "documentation by $documentationSignature already in $LOCAL_PATH"
else
  echo $newline
	echo "make dir $documentationPath..."
	mkdir -p $documentationPath
	echo "move documentation"
	mv $BUILD_DIR/build/documentation/html/* $documentationPath

  echo $newline
	git -C $LOCAL_PATH add --all . || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "adding failed!"
    exit $ILLEGAL_STATE
  fi
	git -C $LOCAL_PATH commit -m "add $documentationSignature documentation" || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "commiting failed!"
    exit $ILLEGAL_STATE
  fi
	git -C $LOCAL_PATH push -f -q || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo "pushing failed!"
    exit $ILLEGAL_STATE
  fi
fi
