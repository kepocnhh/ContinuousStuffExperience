echo "after success..."

if test -z "$PR_NUMBER"; then
  echo "it is not pull request"
  exit 1
fi

if test "$BRANCH_NAME" != "$DEVELOP_BRANCH_NAME"; then
  echo "it is not pull request to branch \"$DEVELOP_BRANCH_NAME\", but to branch \"$BRANCH_NAME\""
  exit 2
fi

if test -z "$PR_SOURCE_BRANCH_NAME"; then
  echo "source branch of pull request #$PR_NUMBER undefined"
  exit 3
fi

echo $newline
echo "it is pull request #$PR_NUMBER $COMMIT -> \"$BRANCH_NAME\""
echo $newline

LOCAL_PATH="~/automerge"

GIT_URL="https://$git_hub_personal_access_token@github.com/$REPO_SLUG"

#__________ __________ cloning >

echo $newline
echo "cloning $REPO_SLUG to $LOCAL_PATH..."
git clone -q --depth=1 --branch="$BRANCH_NAME" $GIT_URL.git $LOCAL_PATH || exit 4

git -C $LOCAL_PATH config user.name "$USER"
git -C $LOCAL_PATH config user.email "$USER"

PR_BRANCH_NAME="pr$PR_NUMBER"

ILLEGAL_STATE=0

#__________ __________ fetching >

echo $newline
echo "fetching origin pull/$PR_NUMBER/head..."
git -C $LOCAL_PATH fetch origin pull/$PR_NUMBER/head:$PR_BRANCH_NAME || ILLEGAL_STATE=$?
if test $ILLEGAL_STATE -ne 0
then
  echo "Fetching origin pull/$PR_NUMBER/head failed!"
  exit $ILLEGAL_STATE
fi

PULL_REQUEST_COMMIT_MESSAGE="Merge $COMMIT -> \"$BRANCH_NAME\" by $USER"

#__________ __________ merging >

echo $newline
echo "merging $COMMIT -> \"$BRANCH_NAME\"..."
git -C $LOCAL_PATH merge -q --no-ff -m "$PULL_REQUEST_COMMIT_MESSAGE" $PR_BRANCH_NAME || ILLEGAL_STATE=$?
if test $ILLEGAL_STATE -ne 0
then
  echo "Merge $COMMIT -> \"$BRANCH_NAME\" failed!"
  exit $ILLEGAL_STATE
fi

#__________ __________ pushing >

echo $newline
echo "pushing merge $COMMIT -> \"$BRANCH_NAME\"..."
git -C $LOCAL_PATH push -f -q || ILLEGAL_STATE=$?
if test $ILLEGAL_STATE -ne 0
then
  echo "Push merge $COMMIT -> \"$BRANCH_NAME\" failed!"
  exit $ILLEGAL_STATE
fi
