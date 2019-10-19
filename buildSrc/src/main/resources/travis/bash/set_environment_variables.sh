echo "set environment variables..."

export newline=$'\n'

export BUILD_SUCCESS=0

export REPO_OWNER=${TRAVIS_REPO_SLUG%/*}
export REPO_NAME=${TRAVIS_REPO_SLUG#*/}
export REPO_OWNER_URL="https://github.com/$REPO_OWNER"
export REPO_URL="https://github.com/$TRAVIS_REPO_SLUG"
export GIT_HUB_PAGES_URL="https://${REPO_OWNER}.github.io/$REPO_NAME"
export TRAVIS_URL="https://travis-ci.com/$TRAVIS_REPO_SLUG"

export DEVELOP_BRANCH_NAME="dev"
export MASTER_BRANCH_NAME="master"

export TESTING_STATUS=0
export VERIFY_TEST_COVERAGE_STATUS=0
export VERIFY_DOCUMENTATION_STATUS=0
export VERIFY_STYLE_STATUS=0
export VERIFY_WARNING_STATUS=0
export VERIFY_README_STATUS=0
export GH_PAGES_REPORT_STATUS=0

export TEST_COVERAGE_REPORT_URL=""
export TESTING_REPORT_URL=""
export DOCUMENTATION_URL=""

export emoji_heavy_check_mark="%E2%9C%94%EF%B8%8F"
export emoji_heavy_exclamation_mark="%E2%9D%97%EF%B8%8F"
export emoji_grey_exclamation="%E2%9D%95"

TRAVIS_MESSAGE="Travis build [#$TRAVIS_BUILD_NUMBER]($TRAVIS_URL/builds/$TRAVIS_BUILD_ID)"
REPOSITORY_MESSAGE="Repository [$REPO_NAME]($REPO_URL) of [$REPO_OWNER]($REPO_OWNER_URL)"
COMMIT_MESSAGE="commit [${TRAVIS_COMMIT::7}]($REPO_URL/commit/$TRAVIS_COMMIT) \"$TRAVIS_COMMIT_MESSAGE\""
export TELEGRAM_MESSAGE_PREFIX="$TRAVIS_MESSAGE${newline}${newline}$REPOSITORY_MESSAGE${newline}${newline}$COMMIT_MESSAGE"