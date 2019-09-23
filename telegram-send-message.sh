echo "telegram send message..."

if [ $# -ne 4 ]; then
    echo "Script needs for 4 arguments"
    exit 1
fi

TELEGRAM_BOT_ID=$1
TELEGRAM_BOT_TOKEN=$2
TELEGRAM_CHAT_ID=$3
#MESSAGE=$4

url="https://api.telegram.org/bot$TELEGRAM_BOT_ID:$TELEGRAM_BOT_TOKEN/sendMessage"

RESPONSE_CODE=$(
	curl --write-out '%{http_code}\n' -X GET -G \
		--silent --output /dev/null \
		$url \
		-d chat_id=$TELEGRAM_CHAT_ID \
		-d text="$MESSAGE" \
		-d parse_mode=markdown || exit 2
)

if [ $RESPONSE_CODE -ne 200 ]
then
	exit 3
fi