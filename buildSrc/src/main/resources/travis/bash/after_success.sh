echo "after success..."

if [[ $TRAVIS_PULL_REQUEST =~ ^[0-9]+$ ]]
then
  if test "$TRAVIS_PULL_REQUEST_BRANCH" != "$DEVELOP_BRANCH_NAME"
  then
    echo "it is not pull request to branch \"$DEVELOP_BRANCH_NAME\" but to branch \"$TRAVIS_PULL_REQUEST_BRANCH\""
    exit 0
  fi
else
  echo "it is not pull request"
  exit 0
fi

echo "it is pull request #$TRAVIS_PULL_REQUEST to branch \"$TRAVIS_PULL_REQUEST_BRANCH\""

LOCAL_PATH="~/automerge"

echo "cloning $TRAVIS_REPO_SLUG to $LOCAL_PATH..."
git clone --depth=1 --branch="$DEVELOP_BRANCH_NAME" $REPO_URL.git $LOCAL_PATH || exit 1

git -C ~/gh-pages config user.name "$USER"
git -C ~/gh-pages config user.email "$USER"

echo "after success WIP"
