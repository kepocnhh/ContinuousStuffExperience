echo "before script..."

echo "commit: $TRAVIS_COMMIT"
echo "commit message: $TRAVIS_COMMIT_MESSAGE"
echo "branch: $TRAVIS_BRANCH"

if [[ $TRAVIS_PULL_REQUEST =~ ^[0-9]+$ ]]
then
  TELEGRAM_MESSAGE_PREFIX+="${newline}pull request [#$TRAVIS_PULL_REQUEST]($REPO_URL/pull/$TRAVIS_PULL_REQUEST)"
else
  echo "it is not a pull request"
fi

COMPILE_STATUS=0

gradle compile || COMPILE_STATUS=$?

if [[ COMPILE_STATUS -ne 0 ]]
then
  MESSAGE="$TELEGRAM_MESSAGE_PREFIX${newline}${newline}not compiled %E2%9D%97%EF%B8%8F"
  bash telegram-send-message.sh $telegram_bot_id $telegram_bot_token $telegram_chat_id "$MESSAGE"
  exit $COMPILE_STATUS
fi
