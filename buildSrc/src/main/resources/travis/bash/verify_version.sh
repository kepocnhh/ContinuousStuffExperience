echo "verify version..."

if [[ "$0" != "$BASH_SOURCE" ]]; then
  EXIT=return
else
  EXIT=exit
fi

task_name=":version"
rootProjectVersion=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task_name\" must be completed successfully for destination."
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi
if [[ "$rootProjectVersion" == "" ]]; then
  echo "Root project version must be not empty"
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi
ROOT_PROJECT_VERSION=$rootProjectVersion

if test -z "$PR_NUMBER"; then
  echo "it is not pull request"
  VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_SUCCESS
  $EXIT 0
fi

if [[ "$BRANCH_NAME" != "$DEVELOP_BRANCH_NAME" ]]; then
  echo "it is not pull request to branch \"$DEVELOP_BRANCH_NAME\" but to branch \"$BRANCH_NAME\""
  VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_SUCCESS
  $EXIT 0
fi

if test -z "$PR_SOURCE_BRANCH_NAME"; then
  echo "source branch of pull request #$PR_NUMBER undefined"
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi

echo $newline
echo "it is pull request #$PR_NUMBER \"$PR_SOURCE_BRANCH_NAME\" -> \"$BRANCH_NAME\""
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
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task_name\" must be completed successfully for destination."
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi

allProjectsSrc=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task_name\" must be completed successfully for source."
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi

if [[ "$allProjectsDst" != "$allProjectsSrc" ]]; then
  echo "To verify the version, it is necessary that the projects coincide."
  $EXIT 0
fi

task_name="versions"
versionsDst=$(gradle -p $LOCAL_PATH -q $task_name) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task_name\" must be completed successfully for destination."
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi

versionsSrc=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task_name\" must be completed successfully for source."
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi

if [[ "$versionsDst" == "$versionsSrc" ]]; then
  echo "The version must be changed relative to the previous pull request."
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi

task_name=":protectedName"
rootProjectProtectedName=$(gradle -q $task_name) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task_name\" must be completed successfully."
  $EXIT 1
fi

lines=($(git diff --name-only origin/$BRANCH_NAME))

if [ -z "${lines//$' '/""}" ] || [ ${#lines[@]} -eq 0 ]; then
  echo "lines must be not empty"
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
fi

for line in ${lines[@]}; do
  if [[ "$line" == "" ]]; then
    echo "line must be not empty"
    VERIFY_VERSION_STATUS="error"
    $EXIT 1
  fi
done

function isProjectChanged {
  if test $# -ne 1; then
      echo "Script needs for 1 arguments but actual $#"
      return 1
  fi

  project=$1

  if [[ "$rootProjectProtectedName" == "$project" ]]; then
    echo "true"
    return 0
  fi

  for line in ${lines[@]}; do
    if [[ "$rootProjectProtectedName/$line" == "$project/"* ]]; then
      echo "true"
      return 0
    fi
  done

  echo "false"
  return 0
}

function isVersionChanged {
  if test $# -ne 1; then
      echo "Script needs for 1 arguments but actual $#"
      return 1
  fi

  project=$1

  for versionsDst in ${versionsDst[@]}; do
    if [[ "$versionsDst" == "$project:"* ]]; then
      for versionsSrc in ${versionsSrc[@]}; do
        if [[ "$versionsSrc" == "$project:"* ]]; then
          if [[ "$versionsDst" != "$versionsSrc" ]]; then
            echo "true"
            return 0
          else
            echo "false"
            return 0
          fi
        fi
      done
    fi
  done

  echo "version for project \"$project\" must exist"
  VERIFY_VERSION_STATUS="error"
  $EXIT 1
}

for project in ${allProjectsDst[@]}; do
  if [[ "$(isProjectChanged $project)" == "true" ]]; then
    if [[ "$(isVersionChanged $project)" != "true" ]]; then
      echo "version of project $project must be changed"
      VERIFY_VERSION_STATUS="error"
      $EXIT 1
    fi
  else
    if [[ "$(isVersionChanged $project)" == "true" ]]; then
      echo "version of project $project must be NOT changed"
      VERIFY_VERSION_STATUS="error"
      $EXIT 1
    fi
  fi
done

VERIFY_VERSION_STATUS=$VERIFY_VERSION_STATUS_SUCCESS
$EXIT 0
