echo "after success..."

if [[ $TRAVIS_PULL_REQUEST =~ ^[0-9]+$ ]]
then
  if test "$TRAVIS_BRANCH" != "$DEVELOP_BRANCH_NAME"
  then
    echo "it is not pull request to branch \"$DEVELOP_BRANCH_NAME\" but to branch \"$TRAVIS_BRANCH\""
    exit 0
  fi
  if test "$TRAVIS_PULL_REQUEST_BRANCH" == ""
  then
    echo "source branch of pull request #$TRAVIS_PULL_REQUEST undefined"
    exit 0
  fi
else
  echo "it is not pull request"
  exit 0
fi

echo "it is pull request #$TRAVIS_PULL_REQUEST \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\""

LOCAL_PATH="~/automerge"

echo "cloning $TRAVIS_REPO_SLUG to $LOCAL_PATH..."
git clone --depth=1 --branch="$TRAVIS_BRANCH" $REPO_URL.git $LOCAL_PATH || exit 1

git -C $LOCAL_PATH config user.name "$USER"
git -C $LOCAL_PATH config user.email "$USER"

PULL_REQUEST_BRANCH_NAME="pr$TRAVIS_PULL_REQUEST"

git -C $LOCAL_PATH fetch origin pull/$TRAVIS_PULL_REQUEST/head:$PULL_REQUEST_BRANCH_NAME

PULL_REQUEST_COMMIT_MESSAGE="Merge \"$TRAVIS_PULL_REQUEST_BRANCH\" -> \"$TRAVIS_BRANCH\" by $USER"

git -C $LOCAL_PATH merge --no-ff -m "$PULL_REQUEST_COMMIT_MESSAGE" $PULL_REQUEST_BRANCH_NAME
git -C $LOCAL_PATH push -f -q -u "https://$git_hub_personal_access_token@github.com/$TRAVIS_REPO_SLUG" $TRAVIS_BRANCH