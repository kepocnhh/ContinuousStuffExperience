echo "assembly..."

ILLEGAL_STATE=0

task="gradle -q allProjectsForAssembly"
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
  bash ${BASH_PATH}/assembly/deploy_project.sh "$project" || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo -e "deploy project \"$project\" \033[91;1mfailed\033[0m"
    exit $ILLEGAL_STATE
  fi
done