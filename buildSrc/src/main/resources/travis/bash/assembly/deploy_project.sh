echo "deploy project..."

if test $# -ne 1; then
    echo "Script needs for 1 arguments but actual $#"
    exit 1
fi

project=$1

if [ -z "${project//$' '/""}" ] || [ "${project//$' '/""}" != "$project" ]; then
  echo "project must be not empty"
  exit 1
fi

bucket="continuousstuffexperience.appspot.com"
baseUrl="https://firebasestorage.googleapis.com/v0/b/$bucket/o"

ILLEGAL_STATE=0

pathRemote="assembly/$BRANCH_NAME"
if [ "$BRANCH_NAME" != "$DEVELOP_BRANCH_NAME" ]; then # todo master branch

  task="git merge-base head origin/$DEVELOP_BRANCH_NAME"
  mergeBase=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    exit $ILLEGAL_STATE
  fi
  if test -z "$mergeBase"; then
    echo "merge base must be not empty"
    exit 1
  fi

  pathRemote="$pathRemote/$mergeBase"
fi

task="gradle -q clean ${project}:assemble"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi

task="gradle -q ${project}:version"
projectVersion=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi
if test -z "$projectVersion"; then
  echo "Project version by \"$project\" must be not empty"
  exit 2
fi

task="gradle -q ${project}:simpleName"
projectName=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi
if test -z "$projectName"; then
  echo "Project name by \"$project\" must be not empty"
  exit 3
fi

task="gradle -q ${project}:simpleName"
projectName=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi
if test -z "$projectName"; then
  echo "Project name by \"$project\" must be not empty"
  exit 4
fi

projectPath=${project//://}
fileName="$projectName-$projectVersion.jar"
filePath=".$projectPath/build/libs/$fileName"
if test -f $filePath; then
  echo "File by path \"$filePath\" exists"
else
  echo "File by path \"$filePath\" must be exists"
  exit 5
fi

filePathRemote="$pathRemote$projectPath/$projectVersion/$fileName"
url="$baseUrl?name=$filePathRemote"
RESPONSE_CODE=$(
  curl -w '%{http_code}\n' -X DELETE \
    --silent --output /dev/null \
    $url
)
if [ "$RESPONSE_CODE" == "204" ]; then
  echo -e "delete remote file \"$filePathRemote\" \033[32;1msuccess\033[0m"
elif [ "$RESPONSE_CODE" == "404" ]; then
  echo "remote file \"$filePathRemote\" does not exist yet"
else
  echo "response code $RESPONSE_CODE but expected 204 or 404"
  echo -e "so delete remote file \"$filePathRemote\" \033[91;1mfailed\033[0m"
  exit 6
fi

RESPONSE_CODE=$(
  curl -w '%{http_code}\n' -X POST --data-binary @$filePath \
    --silent --output /dev/null \
    $url
)
if [ "$RESPONSE_CODE" == "200" ]; then
  echo -e "upload file by path \"$filePath\" \033[32;1msuccess\033[0m"
else
  echo "response code $RESPONSE_CODE but expected 200"
  echo -e "so delete file by path \"$filePath\" \033[91;1mfailed\033[0m"
  exit 7
fi
