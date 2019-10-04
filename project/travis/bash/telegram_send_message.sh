echo "telegram send message..."

if [ $# -ne 4 ]; then
    echo "Script needs for 4 arguments but actual $#"
    exit 1
fi

TELEGRAM_BOT_ID=$1
TELEGRAM_BOT_TOKEN=$2
TELEGRAM_CHAT_ID=$3
MESSAGE=$4

url="https://api.telegram.org/bot$TELEGRAM_BOT_ID:$TELEGRAM_BOT_TOKEN/sendMessage"

MESSAGE=${MESSAGE//"#"/"%23"}
MESSAGE=${MESSAGE//$'\n'/"%0A"}
MESSAGE=${MESSAGE//$'\r'/""}

RESPONSE_CODE=$(
	curl -w '%{http_code}\n' -X GET -G \
		--silent --output /dev/null \
		$url \
		-d chat_id=$TELEGRAM_CHAT_ID \
		-d text="$MESSAGE" \
		-d parse_mode=markdown
)

if [ "$RESPONSE_CODE" != "200" ]
then
	echo "response code $RESPONSE_CODE but expected 200"
	exit 3
fi

echo "message: $MESSAGE sent by telegram bot"
