echo "before script..."

echo "commit: $TRAVIS_COMMIT"
echo "commit message: $TRAVIS_COMMIT_MESSAGE"
echo "branch: $TRAVIS_BRANCH"

if test -z "$BASH_PATH"; then
  echo "Variable \"BASH_PATH\" is not set"
  exit 1
fi

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
  bash ${BASH_PATH}/telegram-send-message.sh "script failed $emoji_heavy_exclamation_mark"
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

gradle -q compile || COMPILE_STATUS=$?

if [[ COMPILE_STATUS -ne 0 ]]
then
  MESSAGE="$TELEGRAM_MESSAGE_PREFIX${newline}${newline}not compiled $emoji_heavy_exclamation_mark"
  bash ${BASH_PATH}/telegram-send-message.sh "$MESSAGE"
  exit $COMPILE_STATUS
fi
