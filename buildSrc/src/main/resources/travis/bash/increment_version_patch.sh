echo "increment version patch..."

if test $# -ne 2; then
    echo "Script needs for 2 arguments but actual $#"
    exit 1
fi

modules=($1)
lines=($2)

rootVersionIncremented=0

if [ -z "${modules//$' '/""}" ] || [ ${#modules[@]} -eq 0 ]; then
  echo "modules must be not empty"
  exit 1
fi

if [ -z "${lines//$' '/""}" ] || [ ${#lines[@]} -eq 0 ]; then
  echo "lines must be not empty"
  exit 2
fi

for line in ${lines[@]}; do
  isDiffInModule=0
  for module in ${modules[@]}; do
    if [[ $line == "$module/"* ]]; then
      gradle -q :$module:incrementVersionPatch
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
    gradle -q :incrementVersionPatch
    echo "version of root module incremented"
    rootVersionIncremented=1
  fi
  if [ ${#modules[@]} -eq 0 ] && [ $rootVersionIncremented -ne 0 ]; then
    echo "versions of all modules incremented"
    exit 0
  fi
done
