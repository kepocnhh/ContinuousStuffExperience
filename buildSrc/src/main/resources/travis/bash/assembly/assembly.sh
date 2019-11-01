echo "assembly..."

LOCAL_PATH="~/assembly"

GIT_URL="https://github.com/$REPO_SLUG"

ILLEGAL_STATE=0

task="rm -R -f $LOCAL_PATH"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi

echo $newline
echo "cloning $REPO_SLUG to $LOCAL_PATH..."
task="git clone -q --depth=1 --no-single-branch --branch=$BRANCH_NAME $GIT_URL.git $LOCAL_PATH"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi

task="git -C $LOCAL_PATH pull origin $BRANCH_NAME --unshallow"
$task || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi

task="gradle -p $LOCAL_PATH -q allProjectsForAssembly"
projects=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit $ILLEGAL_STATE
fi

if [ -z "${projects//$' '/""}" ] || [ ${#projects[@]} -eq 0 ]; then
  echo "projects must be not empty"
  exit 2
fi

for project in ${projects[@]}; do
  if test -z "$project"; then
    echo "project must be not empty"
    exit 3
  fi
done

for project in ${projects[@]}; do
  bash ${BASH_PATH}/assembly/deploy_project.sh "$LOCAL_PATH" "$project" || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo -e "deploy project \"$project\" \033[91;1mfailed\033[0m"
    exit $ILLEGAL_STATE
  fi
done