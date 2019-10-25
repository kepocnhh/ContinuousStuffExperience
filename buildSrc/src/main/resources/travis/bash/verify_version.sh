echo "verify version..."

if [[ "$0" != "$BASH_SOURCE" ]]; then
  EXIT=return
else
  EXIT=exit
fi

if test -z "$PR_NUMBER"; then
  echo "it is not pull request"
  VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_SUCCESS
  $EXIT 0
fi

if test "$BRANCH_NAME" != "$DEVELOP_BRANCH_NAME"; then
  echo "it is not pull request to branch \"$DEVELOP_BRANCH_NAME\" but to branch \"$BRANCH_NAME\""
  VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_SUCCESS
  $EXIT 0
fi

if test -z "$PR_SOURCE_BRANCH_NAME"; then
  echo "source branch of pull request #$PR_NUMBER undefined"
  VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_SUCCESS
  $EXIT 0
fi

echo $newline
echo "it is pull request #$PR_NUMBER $COMMIT -> \"$BRANCH_NAME\""
echo $newline

LOCAL_PATH="~/version"

GIT_URL="https://$git_hub_personal_access_token@github.com/$REPO_SLUG"

VERIFY_VERSION_STATUS="error"
echo $newline
echo "cloning $REPO_SLUG to $LOCAL_PATH..."
git clone -q --depth=1 --branch="$BRANCH_NAME" $GIT_URL.git $LOCAL_PATH || $EXIT 1
VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_UNKNOWN

task_name="allProjectsWithVersion"
allProjectsDst=$(gradle -p $LOCAL_PATH -q $task_name) || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "Task \"$task_name\" must be completed successfully for destination."
  $EXIT 0
fi

allProjectsSrc=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "Task \"$task_name\" must be completed successfully for source."
  $EXIT 0
fi

if [[ "$allProjectsDst" != "$allProjectsSrc" ]]; then
  echo "To verify the version, it is necessary that the projects coincide."
  $EXIT 0
fi

task_name="versions"
versionsDst=$(gradle -p $LOCAL_PATH -q $task_name) || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "Task \"$task_name\" must be completed successfully for destination."
  $EXIT 0
fi

versionsSrc=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "Task \"$task_name\" must be completed successfully for source."
  $EXIT 0
fi

if [[ "$versionsDst" == "$versionsSrc" ]]; then
  echo "The version must be changed relative to the previous pull request."
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi
