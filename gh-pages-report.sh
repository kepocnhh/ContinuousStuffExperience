echo "cloning ${TRAVIS_REPO_SLUG} to ~/gh-pages..."
git clone --depth=1 --branch=gh-pages https://github.com/${TRAVIS_REPO_SLUG}.git ~/gh-pages

git -C ~/gh-pages config user.name "${USER}"
git -C ~/gh-pages config user.email "${USER}"

signature=$(<"${TRAVIS_BUILD_DIR}/build/reports/coverage/signature")

path="$HOME/gh-pages/reports/coverage/${signature}"
if [ -d $path ]
then
    echo "test coverage report by ${signature} already in gh-pages"
    exit
fi

echo "make dir $path..."
mkdir -p $path
echo "move test coverage report"
mv ${TRAVIS_BUILD_DIR}/build/reports/coverage/html* $path

git -C ~/gh-pages add --all .
git -C ~/gh-pages commit -m "add ${signature} test coverage report"
git -C ~/gh-pages push -f -q -u "https://${git_hub_personal_access_token}@github.com/${TRAVIS_REPO_SLUG}" gh-pages