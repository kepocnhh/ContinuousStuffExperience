echo "after script..."

TESTING_MESSAGE="- testing: "
if [ $TESTING_STATUS -ne 0 ]
then
  TESTING_MESSAGE+="failed"
else
  TESTING_MESSAGE+="passed"
fi
if test -z "$TESTING_REPORT_URL" 
then
  TESTING_MESSAGE+=" | no report provided"
else
  TESTING_MESSAGE+=" | [report]($TESTING_REPORT_URL)"
fi

TEST_COVERAGE_MESSAGE="- test coverage: verification"
if [ $TEST_COVERAGE_VERIFICATION_STATUS -ne 0 ]
then
  TEST_COVERAGE_MESSAGE+=" failed"
else
  TEST_COVERAGE_MESSAGE+=" passed"
fi
if test -z "$TEST_COVERAGE_REPORT_URL"
then
  TEST_COVERAGE_MESSAGE+=" | no report provided"
else
  TEST_COVERAGE_MESSAGE+=" | [report]($TEST_COVERAGE_REPORT_URL)"
fi

DOCUMENTATION_MESSAGE="- documentation"
if [ DOCUMENTATION_VERIFICATION_STATUS -ne 0 ]
then
  DOCUMENTATION_MESSAGE+=" is not complete"
else
  DOCUMENTATION_MESSAGE+=" is complete"
fi
if test -z "DOCUMENTATION_URL"
then
  DOCUMENTATION_MESSAGE+=" | no provided"
else
  DOCUMENTATION_MESSAGE+=" | [link]($DOCUMENTATION_URL)"
fi

MESSAGE="$TELEGRAM_MESSAGE_PREFIX${newline}${newline}$TESTING_MESSAGE${newline}$TEST_COVERAGE_MESSAGE${newline}$DOCUMENTATION_MESSAGE"
bash telegram-send-message.sh $telegram_bot_id $telegram_bot_token $telegram_chat_id "$MESSAGE"
