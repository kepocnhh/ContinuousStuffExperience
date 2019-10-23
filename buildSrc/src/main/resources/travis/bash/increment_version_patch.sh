echo "increment version patch..."

if test $# -ne 1; then
    echo "Script needs for 1 arguments but actual $#"
    exit 1
fi

LOCAL_PATH=$1

if [ -z "${LOCAL_PATH//$' '/""}" ]; then
  echo "LOCAL_PATH must be not empty"
  exit 1
fi

modules=($(gradle -p $LOCAL_PATH -q subprojects))
lines=($(git -C $LOCAL_PATH diff --name-only origin/$TRAVIS_BRANCH $TRAVIS_BRANCH))

if [ -z "${modules//$' '/""}" ] || [ ${#modules[@]} -eq 0 ]; then
  echo "modules must be not empty"
  exit 1
fi

if [ -z "${lines//$' '/""}" ] || [ ${#lines[@]} -eq 0 ]; then
  echo "lines must be not empty"
  exit 2
fi

rootVersionIncremented=0

for line in ${lines[@]}; do
  isDiffInModule=0
  for module in ${modules[@]}; do
    if [[ $line == "$module/"* ]]; then
      gradle -p $LOCAL_PATH -q :$module:incrementVersionPatch
      echo "version of \"$module\" module incremented"
      for i in "${!modules[@]}"; do
        if [[ ${modules[i]} == $module ]]; then
          unset "modules[i]"
        fi
      done
      isDiffInModule=1
      break
    fi
  done
  if [ $rootVersionIncremented -eq 0 ] && [ $isDiffInModule -eq 0 ]; then
    gradle -p $LOCAL_PATH -q :incrementVersionPatch
    echo "version of root module incremented"
    rootVersionIncremented=1
  fi
  if [ ${#modules[@]} -eq 0 ] && [ $rootVersionIncremented -ne 0 ]; then
    echo "versions of all modules incremented"
    break
  fi
done

git -C $LOCAL_PATH -q add --all . || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]
then
  echo "adding failed!"
  exit $ILLEGAL_STATE
fi

git -C $LOCAL_PATH -q commit -m "Increment version patch by $USER" || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]
then
  echo "commiting failed!"
  exit $ILLEGAL_STATE
fi
