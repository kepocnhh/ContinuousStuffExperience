echo "deploy project..."

if test $# -ne 2; then
    echo "Script needs for 2 arguments but actual $#"
    exit 1
fi

LOCAL_PATH=$1

if [ -z "${LOCAL_PATH//$' '/""}" ] || [ "${LOCAL_PATH//$' '/""}" != "$LOCAL_PATH" ]; then
  echo "local path must be not empty"
  exit 1
fi

project=$2

if [ -z "${project//$' '/""}" ] || [ "${project//$' '/""}" != "$project" ]; then
  echo "project must be not empty"
  exit 1
fi

bucket="continuousstuffexperience.appspot.com"
baseUrl="https://firebasestorage.googleapis.com/v0/b/$bucket/o"

ILLEGAL_STATE=0

pathRemote="assembly/$BRANCH_NAME"
if [ "$BRANCH_NAME" != "$DEVELOP_BRANCH_NAME" ]; then # todo master branch

  task="git -C $LOCAL_PATH merge-base $BRANCH_NAME origin/$DEVELOP_BRANCH_NAME"
  mergeBase=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for deploy project."
    exit $ILLEGAL_STATE
  fi
  if test -z "$mergeBase"; then
    echo "merge base must be not empty"
    exit 1
  fi

  pathRemote="$pathRemote/$mergeBase"
fi

task="gradle -p $LOCAL_PATH -q clean ${project}:assemble"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for deploy project."
  exit $ILLEGAL_STATE
fi

task="gradle -p $LOCAL_PATH -q ${project}:version"
projectVersion=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for deploy project."
  exit $ILLEGAL_STATE
fi
if test -z "$projectVersion"; then
  echo "Project version by \"$project\" must be not empty"
  exit 2
fi

task="gradle -p $LOCAL_PATH -q ${project}:simpleName"
projectName=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for deploy project."
  exit $ILLEGAL_STATE
fi
if test -z "$projectName"; then
  echo "Project name by \"$project\" must be not empty"
  exit 3
fi

projectPath=${project//://}
fileName="$projectName-$projectVersion.jar"
filePath="$LOCAL_PATH$projectPath/build/libs/$fileName"
if test -f $filePath; then
  echo "File by path \"$filePath\" exists"
else
  echo "File by path \"$filePath\" must be exists"
  exit 5
fi

SERVICE_ACCOUNT_TOKEN=""
url="https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$firebase_server_key"
requestBody="{
\"email\":\"$service_account_email\",
\"password\":\"$service_account_password\",
\"returnSecureToken\":true
}"
outputFilePath="/tmp/output"
RESPONSE_CODE=$(
  curl -w '%{http_code}\n' -X POST \
    -s -o "$outputFilePath" \
    $url \
    -H 'Content-Type: application/json' \
    -d "$requestBody"
)
if [ "$RESPONSE_CODE" == "200" ]; then
  responseBody=$(<"$outputFilePath")
  rightPart=${responseBody/*"\"idToken\""/}
  rightPart=${rightPart#*\"}
  SERVICE_ACCOUNT_TOKEN="${rightPart%%\"*}"
else
  echo "response code $RESPONSE_CODE but expected 200"
  echo -e "sign in service account \033[91;1mfailed\033[0m"
  exit 6
fi
rm "$outputFilePath"

if test -z "$SERVICE_ACCOUNT_TOKEN"; then
  echo "Service account token must be not empty"
  exit 6
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
    -H "Authorization: Bearer $SERVICE_ACCOUNT_TOKEN" \
    $url
)
if [ "$RESPONSE_CODE" == "200" ]; then
  echo -e "upload file by path \"$filePath\" \033[32;1msuccess\033[0m"
else
  echo "response code $RESPONSE_CODE but expected 200"
  echo -e "so delete file by path \"$filePath\" \033[91;1mfailed\033[0m"
  exit 7
fi
