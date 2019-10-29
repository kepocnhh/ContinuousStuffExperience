echo "after failure..."

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

gh_url="https://api.github.com"
url="${gh_url}/repos/$REPO_SLUG/pulls/$PR_NUMBER"
body="{\"state\":\"closed\"}"

RESPONSE_CODE=$(
	curl -u $USER:$git_hub_personal_access_token \
	  -w '%{http_code}\n' -X PATCH \
		--silent --output /dev/null \
		$url \
		-d "$body"
)

if [ "$RESPONSE_CODE" != "200" ]
then
	echo "response code $RESPONSE_CODE but expected 200"
  echo "so pull request #$PR_NUMBER failed but not rejected!"
	exit 4
fi

echo "pull request #$PR_NUMBER was successfully rejected as failed"
