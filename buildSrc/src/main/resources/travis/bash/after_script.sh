echo "after script..."

TESTING_MESSAGE="- testing "
if [ $TESTING_STATUS -ne 0 ]
then
  TESTING_MESSAGE+=$emoji_heavy_exclamation_mark
else
  TESTING_MESSAGE+=$emoji_heavy_check_mark
fi
if test -z "$TESTING_REPORT_URL" 
then
  echo "no testing report provided"
else
  TESTING_MESSAGE+=" [report]($TESTING_REPORT_URL)"
fi

echo $TESTING_MESSAGE

TEST_COVERAGE_MESSAGE="- test coverage "
if [ $TEST_COVERAGE_VERIFICATION_STATUS -ne 0 ]
then
  TEST_COVERAGE_MESSAGE+=$emoji_heavy_exclamation_mark
else
  TEST_COVERAGE_MESSAGE+=$emoji_heavy_check_mark
fi
if test -z "$TEST_COVERAGE_REPORT_URL"
then
  echo "no test coverage report provided"
else
  TEST_COVERAGE_MESSAGE+=" [report]($TEST_COVERAGE_REPORT_URL)"
fi

echo $TEST_COVERAGE_MESSAGE

DOCUMENTATION_MESSAGE="- documentation "
if [ $DOCUMENTATION_VERIFICATION_STATUS -ne 0 ]
then
  DOCUMENTATION_MESSAGE+=$emoji_grey_exclamation
else
  DOCUMENTATION_MESSAGE+=$emoji_heavy_check_mark
fi
if test -z "DOCUMENTATION_URL"
then
  echo "no documentation provided"
else
  DOCUMENTATION_MESSAGE+=" [link]($DOCUMENTATION_URL)"
fi

echo $DOCUMENTATION_MESSAGE

STYLE_MESSAGE="- style "
if [ $STYLE_VERIFICATION_STATUS -ne 0 ]
then
  STYLE_MESSAGE+=$emoji_heavy_exclamation_mark
else
  STYLE_MESSAGE+=$emoji_heavy_check_mark
fi

echo $STYLE_MESSAGE

MESSAGE="$TELEGRAM_MESSAGE_PREFIX"
MESSAGE+="${newline}${newline}"
MESSAGE+="$TESTING_MESSAGE"
MESSAGE+="${newline}"
MESSAGE+="$TEST_COVERAGE_MESSAGE"
MESSAGE+="${newline}"
MESSAGE+="$DOCUMENTATION_MESSAGE"
MESSAGE+="${newline}"
MESSAGE+="$STYLE_MESSAGE"
bash ${BASH_PATH}/telegram_send_message.sh "$MESSAGE"
