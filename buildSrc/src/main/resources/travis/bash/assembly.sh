echo "assembly..."

ILLEGAL_STATE=0

task="gradle -q allProjectsForAssembly"
projects=$($task) || ILLEGAL_STATE=$?
if [[ $ILLEGAL_STATE -ne 0 ]]; then
  echo "Task \"$task\" must be completed successfully for assembly."
  exit 1
fi

if [ -z "${projects//$' '/""}" ] || [ ${#projects[@]} -eq 0 ]; then
  echo "projects must be not empty"
  exit 1
fi

for project in ${projects[@]}; do
  if test -z "$project"; then
    echo "project must be not empty"
    exit 1
  fi
done

for project in ${projects[@]}; do
  task="gradle -q clean ${project}:assemble"
  $task || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    exit 1
  fi

  task="gradle -q ${project}:version"
  projectVersion=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    exit 1
  fi
  if test -z "$projectVersion"; then
    echo "Project version by \"$project\" must be not empty"
    exit 1
  fi

  task="gradle -q ${project}:simpleName"
  projectName=$($task) || ILLEGAL_STATE=$?
  if [[ $ILLEGAL_STATE -ne 0 ]]; then
    echo "Task \"$task\" must be completed successfully for assembly."
    exit 1
  fi
  if test -z "$projectName"; then
    echo "Project name by \"$project\" must be not empty"
    exit 1
  fi

  projectPath=".${project//://}"
  filePath="$projectPath/build/libs/$projectName-$projectVersion.jar"
  if test -f $filePath; then
    echo "File by path \"$filePath\" exists"
  else
    echo "File by path \"$filePath\" must be exists"
    exit 1
  fi
done
