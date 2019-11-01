echo "deploy summary..."

if [[ "$0" != "$BASH_SOURCE" ]]; then
  EXIT=return
else
  EXIT=exit
fi

LOCAL_PATH="~/deploy/summary"

ILLEGAL_STATE=0

task="rm -R -f $LOCAL_PATH"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi

GIT_URL="https://$git_hub_personal_access_token@github.com/$REPO_SLUG"

# __________ __________ cloning >

echo $newline
echo "cloning $REPO_SLUG to $LOCAL_PATH..."
task="git clone -q --depth=1 --no-single-branch --branch=$BRANCH_NAME $GIT_URL.git $LOCAL_PATH"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  $EXIT $ILLEGAL_STATE
fi

git -C $LOCAL_PATH config user.name "$USER"
git -C $LOCAL_PATH config user.email "$USER"

# __________ __________ >

remotePath="assembly/$BRANCH_NAME"
if [ "$BRANCH_NAME" != "$DEVELOP_BRANCH_NAME" ]; then # todo master branch

  task="git -C $LOCAL_PATH pull origin $BRANCH_NAME --unshallow"
  $task || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    $EXIT $ILLEGAL_STATE
  fi

  task="git -C $LOCAL_PATH pull origin $DEVELOP_BRANCH_NAME"
  $task || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    $EXIT $ILLEGAL_STATE
  fi

  task="git -C $LOCAL_PATH merge-base $BRANCH_NAME origin/$DEVELOP_BRANCH_NAME"
  mergeBase=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    $EXIT $ILLEGAL_STATE
  fi
  if test -z "$mergeBase"; then
    echo "merge base must be not empty"
    $EXIT 1
  fi

  remotePath="$remotePath/$mergeBase"
fi
resultUrl="$GIT_HUB_PAGES_URL/$remotePath/"
localDir="$LOCAL_PATH/$remotePath"
relativePath="$remotePath/index.html"
localPath="$LOCAL_PATH/$relativePath"

text="<html><body><h3><a href=\"$REPO_URL\">$REPO_NAME</a> deploy summary</h3><ul>"

task="gradle -p $LOCAL_PATH -q allProjectsForAssembly"
projects=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for deploy summary."
  $EXIT $ILLEGAL_STATE
fi
if [ -z "${projects//$' '/""}" ] || [ ${#projects[@]} -eq 0 ]; then
  echo "projects must be not empty"
  $EXIT 2
fi
for project in ${projects[@]}; do
  if test -z "$project"; then
    echo "project must be not empty"
    $EXIT 3
  fi
done
bucket="continuousstuffexperience.appspot.com"
baseUrl="https://firebasestorage.googleapis.com/v0/b/$bucket/o"
for project in ${projects[@]}; do

  task="gradle -p $LOCAL_PATH -q ${project}:version"
  projectVersion=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for deploy summary."
    $EXIT $ILLEGAL_STATE
  fi
  if test -z "$projectVersion"; then
    echo "Project version by \"$project\" must be not empty"
    $EXIT 4
  fi

  task="gradle -p $LOCAL_PATH -q ${project}:simpleName"
  projectName=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for deploy project."
    $EXIT $ILLEGAL_STATE
  fi
  if test -z "$projectName"; then
    echo "Project name by \"$project\" must be not empty"
    $EXIT 5
  fi

  projectPath=${project//://}
  fileName="$projectName-$projectVersion.jar"
  filePathRemote="$remotePath$projectPath/$projectVersion/$fileName"

  link="$baseUrl?name=$filePathRemote&alt=media"
  text="$text<li><a href=\"$link\">$projectPath-$projectVersion</a></li>"
done

text="$text</ul></body></html>"

task="git -C $LOCAL_PATH checkout gh-pages"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  $EXIT $ILLEGAL_STATE
fi

if [ -f $localPath ]; then
  oldSummaryData=$(<"$localPath")
  if [ "$oldSummaryData" == "$text" ]; then
    echo "deploy summary by url \"$resultUrl\" already in $LOCAL_PATH"
    DEPLOY_SUMMARY_URL="$resultUrl"
    $EXIT 0
  fi
  echo $newline
  rm $localPath || ILLEGAL_STATE=$?
  if [ $ILLEGAL_STATE -ne 0 ]; then
    echo -e "remove file by path \"$localPath\" \033[91;1mfailed\033[0m"
    $EXIT $ILLEGAL_STATE
  fi
fi

echo "create file by path \"$localPath\"..."
mkdir -p "$localDir"
echo "$text" > "$localPath"

if [ -f $localPath ]; then
  echo -e "create file by path \"$localPath\" \033[32;1msuccess\033[0m"
else
  echo -e "create file by path \"$localPath\" \033[91;1mfailed\033[0m"
  $EXIT 6
fi

echo $newline
git -C $LOCAL_PATH add $relativePath || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "adding failed!"
  $EXIT $ILLEGAL_STATE
fi
git -C $LOCAL_PATH commit -q -m "add deploy summary by url \"$resultUrl\"" || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "commiting failed!"
  $EXIT $ILLEGAL_STATE
fi
git -C $LOCAL_PATH push -f -q || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "pushing failed!"
  $EXIT $ILLEGAL_STATE
fi

DEPLOY_SUMMARY_URL="$resultUrl"
