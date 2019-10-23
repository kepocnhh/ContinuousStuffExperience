echo "after success..."

if [[ $TRAVIS_PULL_REQUEST =~ ^[0-9]+$ ]]
then
  if test "$TRAVIS_BRANCH" != "$DEVELOP_BRANCH_NAME"
  then
    echo "it is not pull request to branch \"$DEVELOP_BRANCH_NAME\" but to branch \"$TRAVIS_BRANCH\""
    exit 1
  fi
  if test "$TRAVIS_PULL_REQUEST_BRANCH" == ""
  then
    echo "source branch of pull request #$TRAVIS_PULL_REQUEST undefined"
    exit 2
  fi
else
  echo "it is not pull request"
  exit 3
fi

echo $newline
echo "it is pull request #$TRAVIS_PULL_REQUEST \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\""
echo $newline

LOCAL_PATH="~/automerge"

GIT_URL="https://$git_hub_personal_access_token@github.com/$TRAVIS_REPO_SLUG"

#__________ __________ cloning >

echo $newline
echo "cloning $TRAVIS_REPO_SLUG to $LOCAL_PATH..."
git clone -q --depth=1 --branch="$TRAVIS_BRANCH" $GIT_URL.git $LOCAL_PATH || exit 4

git -C $LOCAL_PATH config user.name "$USER"
git -C $LOCAL_PATH config user.email "$USER"

PULL_REQUEST_BRANCH_NAME="pr$TRAVIS_PULL_REQUEST"

ILLEGAL_STATE=0

#__________ __________ fetching >

echo $newline
echo "fetching origin pull/$TRAVIS_PULL_REQUEST/head..."
git -C $LOCAL_PATH fetch origin pull/$TRAVIS_PULL_REQUEST/head:$PULL_REQUEST_BRANCH_NAME || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]
then
  echo "Fetching origin pull/$TRAVIS_PULL_REQUEST/head failed!"
  exit $ILLEGAL_STATE
fi

PULL_REQUEST_COMMIT_MESSAGE="Merge \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\" by $USER"

#__________ __________ merging >

echo $newline
echo "merging \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\"..."
git -C $LOCAL_PATH merge -q --no-ff -m "$PULL_REQUEST_COMMIT_MESSAGE" $PULL_REQUEST_BRANCH_NAME || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]
then
  echo "Merge \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\" failed!"
  exit $ILLEGAL_STATE
fi

#__________ __________ verify diff >

echo $newline
echo "verify diff \"origin/$TRAVIS_BRANCH\" and \"head\"..."

modules=$(gradle -q subprojects)
lines=$(git diff --name-only origin/$TRAVIS_BRANCH $TRAVIS_BRANCH)

echo "modules: [${modules[@]}]"
echo "modules size: ${#modules[@]}"
echo "lines: [${lines[@]}]"
echo "lines size: ${#lines[@]}"

bash ${BASH_PATH}/increment_version_patch.sh "$modules" "$lines"

git -C $LOCAL_PATH add --all . || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]
then
  echo "adding failed!"
  exit $ILLEGAL_STATE
fi

git -C $LOCAL_PATH commit -m "Increment version patch by $USER" || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]
then
  echo "commiting failed!"
  exit $ILLEGAL_STATE
fi

#__________ __________ pushing >

echo $newline
echo "pushing merge \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\"..."
git -C $LOCAL_PATH push -f -q || ILLEGAL_STATE=$?
if [ $ILLEGAL_STATE -ne 0 ]
then
  echo "Push merge \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\" failed!"
  exit $ILLEGAL_STATE
fi
