#!/usr/bin/env bash

ES_INDEX='jobdesk'
ES_ADDRESS='http://localhost:9200'
ES_BASE_URL="${ES_ADDRESS}/${ES_INDEX}"
MAPPING_FILE=$(dirname $0)/../../jobdesk/master/src/main/resources/elasticsearch-jobdesk.json

# Check that Elasticsearch is running
curl -s -X GET "${ES_ADDRESS}" 2>&1 > /dev/null
if [ $? != 0 ]; then
    echo "Unable to contact Elasticsearch on port 9200."
    echo "Please ensure Elasticsearch is running and can be reached at http://localhost:9200/"
    exit -1
fi

# Delete the old index, swallow failures if it doesn't exist
curl -s -X DELETE "'${ES_BASE_URL}'" # > /dev/null

# Debugging check
#curl -s -X GET "'${ES_BASE_URL}/job/_count'"
#curl -s -X GET "'${ES_BASE_URL}/location/_count'"

# Create the next index using mapping.json
echo "Creating '${ES_INDEX}' index..."
curl -s -X POST '$ES_BASE_URL' -d@${MAPPING_FILE}

# Wait for index to become yellow
curl -s "'${ES_BASE_URL}/_health?wait_for_status=yellow&timeout=10s'" > /dev/null
echo "Done creating '${ES_INDEX}' index."

