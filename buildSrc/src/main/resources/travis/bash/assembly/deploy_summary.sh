echo "deploy summary..."

if [[ "$0" != "$BASH_SOURCE" ]]; then
  EXIT=return
else
  EXIT=exit
fi

LOCAL_PATH="~/deploy/summary"

GIT_URL="https://$git_hub_personal_access_token@github.com/$REPO_SLUG"

# __________ __________ cloning >

echo $newline
echo "cloning $REPO_SLUG to $LOCAL_PATH..."
git clone -q --depth=1 --branch=gh-pages $GIT_URL.git $LOCAL_PATH || $EXIT 1

git -C $LOCAL_PATH config user.name "$USER"
git -C $LOCAL_PATH config user.email "$USER"

# __________ __________ >

remotePath="assembly/$BRANCH_NAME"
if [ "$BRANCH_NAME" != "$DEVELOP_BRANCH_NAME" ]; then # todo master branch

  task="git merge-base head origin/$DEVELOP_BRANCH_NAME"
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
localPath="$LOCAL_PATH/$remotePath/index.html"

ILLEGAL_STATE=0

text="<html><body><h3>Deploy summary</h3><ul>"

task="gradle -q allProjectsForAssembly"
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

  task="gradle -q ${project}:version"
  version=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    $EXIT $ILLEGAL_STATE
  fi
  if test -z "$version"; then
    echo "Project version by \"$project\" must be not empty"
    $EXIT 4
  fi

  link="$baseUrl?name=$remotePath/$project/$version/$project-$version.jar"
  text="$text<li><a href=\"$link\">$project-$version</a></li>"
done

text="$text</ul></body></html>"

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
echo "$text" > "$localPath"

if [ -f $localPath ]; then
  echo -e "create file by path \"$localPath\" \033[32;1msuccess\033[0m"
else
  echo -e "create file by path \"$localPath\" \033[91;1mfailed\033[0m"
  $EXIT 5
fi

echo $newline
git -C $LOCAL_PATH add --all . || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]; then
  echo "adding failed!"
  $EXIT $ILLEGAL_STATE
fi
git -C $LOCAL_PATH commit -m "add deploy summary by url \"$resultUrl\"" || ILLEGAL_STATE=$?
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
