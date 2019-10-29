echo "set environment variables..."

export newline=$'\n'
export IS_INTEGER_REGEX="^[1-9][0-9]*$"

export BUILD_SUCCESS=0

export BUILD_DIR=$TRAVIS_BUILD_DIR

export REPO_OWNER=${TRAVIS_REPO_SLUG%/*}
export REPO_NAME=${TRAVIS_REPO_SLUG#*/}
export REPO_SLUG=$TRAVIS_REPO_SLUG
export REPO_OWNER_URL="https://github.com/$REPO_OWNER"
export REPO_URL="https://github.com/$TRAVIS_REPO_SLUG"
export GIT_HUB_PAGES_URL="https://${REPO_OWNER}.github.io/$REPO_NAME"
export TRAVIS_URL="https://travis-ci.com/$TRAVIS_REPO_SLUG"

export PR_NUMBER=""
if [[ $TRAVIS_PULL_REQUEST =~ $IS_INTEGER_REGEX ]]
then
  PR_NUMBER=$TRAVIS_PULL_REQUEST
  echo "pull request number: $PR_NUMBER"
else
  echo "it is not pull request"
fi

export PR_SOURCE_BRANCH_NAME=$TRAVIS_PULL_REQUEST_BRANCH

export COMMIT=$TRAVIS_COMMIT
export COMMIT_MESSAGE=$TRAVIS_COMMIT_MESSAGE
export BRANCH_NAME=$TRAVIS_BRANCH
export DEVELOP_BRANCH_NAME="dev"
export MASTER_BRANCH_NAME="master"

export TESTING_STATUS=0
export VERIFY_TEST_COVERAGE_STATUS=0
export VERIFY_DOCUMENTATION_STATUS=0
export VERIFY_STYLE_STATUS=0
export VERIFY_WARNING_STATUS=0
export VERIFY_README_STATUS=0

export ROOT_PROJECT_VERSION=""

export VERIFY_VERSION_STATUS_SUCCESS="verify_version_status_success"
export VERIFY_VERSION_STATUS_UNKNOWN="verify_version_status_unknown"
export VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_UNKNOWN

export GH_PAGES_REPORT_STATUS=0

export TEST_COVERAGE_REPORT_URL=""
export TESTING_REPORT_URL=""
export DOCUMENTATION_URL=""

export emoji_heavy_check_mark="%E2%9C%94%EF%B8%8F"
export emoji_heavy_exclamation_mark="%E2%9D%97%EF%B8%8F"
export emoji_grey_exclamation="%E2%9D%95"

commit_message_encoded=$COMMIT_MESSAGE
commit_message_encoded=${commit_message_encoded//"_"/"\_"}
commit_message_encoded=${commit_message_encoded//"*"/"\*"}
commit_message_encoded=${commit_message_encoded//"`"/"\`"}
commit_message_encoded=${commit_message_encoded//"+"/"%2B"}

TRAVIS_MESSAGE="Travis build [#$TRAVIS_BUILD_NUMBER]($TRAVIS_URL/builds/$TRAVIS_BUILD_ID)"
REPOSITORY_MESSAGE="Repository [$REPO_NAME]($REPO_URL) of [$REPO_OWNER]($REPO_OWNER_URL)"
TELEGRAM_COMMIT_MESSAGE="commit [${COMMIT::7}]($REPO_URL/commit/$COMMIT) \"$commit_message_encoded\""
export TELEGRAM_MESSAGE_PREFIX="$TRAVIS_MESSAGE"
TELEGRAM_MESSAGE_PREFIX="$TELEGRAM_MESSAGE_PREFIX${newline}${newline}"
TELEGRAM_MESSAGE_PREFIX="$TELEGRAM_MESSAGE_PREFIX$REPOSITORY_MESSAGE"
TELEGRAM_MESSAGE_PREFIX="$TELEGRAM_MESSAGE_PREFIX${newline}${newline}"
TELEGRAM_MESSAGE_PREFIX="$TELEGRAM_MESSAGE_PREFIX$TELEGRAM_COMMIT_MESSAGE"
