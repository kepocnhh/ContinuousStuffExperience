echo "verify version..."

if test $# -ne 1; then
    echo "Script needs for 1 arguments but actual $#"
    exit 1
fi

LOCAL_PATH=$1

if [ -z "${LOCAL_PATH//$' '/""}" ]; then
  echo "LOCAL_PATH must be not empty"
  exit 1
fi

ILLEGAL_STATE=0
task_name="allProjectsWithVersion"
allProjectsDst=$(gradle -p $LOCAL_PATH -q $task_name) || ILLEGAL_STATE=$?
if test $ILLEGAL_STATE -ne 0; then
  echo "Task \"$task_name\" must be completed successfully for destination."
  exit 0
fi

allProjectsSrc=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if test $ILLEGAL_STATE -ne 0; then
  echo "Task \"$task_name\" must be completed successfully for source."
  exit 0
fi

if [[ "$allProjectsDst" != "$allProjectsSrc" ]]; then
  echo "To verify the version, it is necessary that the projects coincide."
  exit 0
fi

task_name="versions"
versionsDst=$(gradle -p $LOCAL_PATH -q $task_name) || ILLEGAL_STATE=$?
if test $ILLEGAL_STATE -ne 0; then
  echo "Task \"$task_name\" must be completed successfully for destination."
  exit 0
fi

versionsSrc=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if test $ILLEGAL_STATE -ne 0; then
  echo "Task \"$task_name\" must be completed successfully for source."
  exit 0
fi

if [[ "$versionsDst" == "$versionsSrc" ]]; then
  echo "The version must be changed relative to the previous pull request."
  VERIFY_VERSION_STATUS="error"
  exit 1
fi
