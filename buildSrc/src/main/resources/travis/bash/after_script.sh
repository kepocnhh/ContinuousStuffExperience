echo "after script..."

TESTING_MESSAGE="- testing "
if [ $TESTING_STATUS -ne 0 ]
then
  TESTING_MESSAGE+=$emoji_heavy_exclamation_mark
else
  TESTING_MESSAGE+=$emoji_heavy_check_mark
fi
if test -z "$TESTING_REPORT_URL"; then
  echo "no testing report provided"
else
  TESTING_MESSAGE+=" [report]($TESTING_REPORT_URL)"
fi

echo $TESTING_MESSAGE

TEST_COVERAGE_MESSAGE="- test coverage "
if [ $VERIFY_TEST_COVERAGE_STATUS -ne 0 ]
then
  TEST_COVERAGE_MESSAGE+=$emoji_heavy_exclamation_mark
else
  TEST_COVERAGE_MESSAGE+=$emoji_heavy_check_mark
fi
if test -z "$TEST_COVERAGE_REPORT_URL"; then
  echo "no test coverage report provided"
else
  TEST_COVERAGE_MESSAGE+=" [report]($TEST_COVERAGE_REPORT_URL)"
fi

echo $TEST_COVERAGE_MESSAGE

MESSAGE="$TELEGRAM_MESSAGE_PREFIX"
MESSAGE+="${newline}${newline}"
MESSAGE+="$TESTING_MESSAGE"
MESSAGE+="${newline}"
MESSAGE+="$TEST_COVERAGE_MESSAGE"

#__________ __________ documentation >

if [ $VERIFY_DOCUMENTATION_STATUS -ne 0 ] && [ -z "$DOCUMENTATION_URL" ]; then
  MESSAGE+="${newline}"
  MESSAGE+="- documentation $emoji_heavy_exclamation_mark"
  echo "documentation is not complete"
  echo "no documentation provided"
elif [ $VERIFY_DOCUMENTATION_STATUS -ne 0 ]; then
  MESSAGE+="${newline}"
  MESSAGE+="- documentation $emoji_grey_exclamation [link]($DOCUMENTATION_URL)"
  echo "documentation is not complete"
elif [ -z "$DOCUMENTATION_URL" ]; then
  MESSAGE+="${newline}"
  MESSAGE+="- documentation $emoji_grey_exclamation"
  echo "no documentation provided"
else
  MESSAGE+="${newline}"
  MESSAGE+="- [documentation]($DOCUMENTATION_URL)"
fi

#__________ __________ documentation <

#__________ __________ code style >

if [ $VERIFY_STYLE_STATUS -ne 0 ]
then
  MESSAGE+="${newline}"
  MESSAGE+="- code style $emoji_heavy_exclamation_mark"
fi

#__________ __________ code style <

#__________ __________ warning >

if [ $VERIFY_WARNING_STATUS -ne 0 ]
then
  MESSAGE+="${newline}"
  MESSAGE+="- warnings in code $emoji_heavy_exclamation_mark"
fi

#__________ __________ warning <

#__________ __________ readme >

if [ $VERIFY_README_STATUS -ne 0 ]
then
  MESSAGE+="${newline}"
  MESSAGE+="- README file is not relevant $emoji_heavy_exclamation_mark"
fi

#__________ __________ version >

if test -z "$PR_NUMBER"; then
  echo "it is not pull request"
else
  if [[ "$BRANCH_NAME" == "$DEVELOP_BRANCH_NAME" ]]; then
    if [[ "$VERIFY_VERSION_STATUS" == "$VERIFY_VERSION_STATUS_SUCCESS" ]]; then
      echo "version ok"
    elif [[ "$VERIFY_VERSION_STATUS" == "$VERIFY_VERSION_STATUS_UNKNOWN" ]]; then
      MESSAGE+="${newline}"
      MESSAGE+="- you must verify the versions manually $emoji_grey_exclamation"
    else
      MESSAGE+="${newline}"
      MESSAGE+="- verify version failed $emoji_heavy_exclamation_mark"
    fi
  else
    echo "it is not pull request to branch \"$DEVELOP_BRANCH_NAME\" but to branch \"$BRANCH_NAME\""
  fi
fi

if [ $BUILD_SUCCESS -ne 0 ]
then
  MESSAGE+="${newline}"
  MESSAGE+="${newline}"
  MESSAGE+="build fail $emoji_heavy_exclamation_mark"
fi

bash ${BASH_PATH}/telegram_send_message.sh "$MESSAGE"
