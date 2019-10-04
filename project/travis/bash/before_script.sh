echo "before script..."

echo "commit: $TRAVIS_COMMIT"
echo "commit message: $TRAVIS_COMMIT_MESSAGE"
echo "branch: $TRAVIS_BRANCH"

SCRIPT_STATUS=0
if test -z "$TELEGRAM_MESSAGE_PREFIX"; then
  echo "Variable \"TELEGRAM_MESSAGE_PREFIX\" is not set"
  SCRIPT_STATUS=1
fi
if test -z "$REPO_URL"; then
  echo "Variable \"REPO_URL\" is not set"
  SCRIPT_STATUS=1
fi
if test -z "$TRAVIS_PULL_REQUEST"; then
  echo "Variable \"TRAVIS_PULL_REQUEST\" is not set"
  SCRIPT_STATUS=1
fi

if test $SCRIPT_STATUS -ne 0; then
  bash telegram-send-message.sh "script failed %E2%9D%97%EF%B8%8F"
  exit $SCRIPT_STATUS
fi

if [[ $TRAVIS_PULL_REQUEST =~ ^[0-9]+$ ]]
then
  TELEGRAM_MESSAGE_PREFIX+="${newline}pull request "
  TELEGRAM_MESSAGE_PREFIX+="[#$TRAVIS_PULL_REQUEST]($REPO_URL/pull/$TRAVIS_PULL_REQUEST)"
else
  echo "it is not a pull request"
fi

COMPILE_STATUS=0

gradle compile || COMPILE_STATUS=$?

if [[ COMPILE_STATUS -ne 0 ]]
then
  MESSAGE="$TELEGRAM_MESSAGE_PREFIX${newline}${newline}not compiled %E2%9D%97%EF%B8%8F"
  bash telegram-send-message.sh "$MESSAGE"
  exit $COMPILE_STATUS
fi
