#!/bin/bash

# echo "Merge Version $1"
# echo "Master Version $2"
# echo "Branch name $3"

# Do some parsing here
MASTER_VERSION=$2
MERGE_VERSION=$1

MASTER_MAJOR=`echo $MASTER_VERSION | cut -d . -f 1`
MASTER_MINOR=`echo $MASTER_VERSION | cut -d . -f 2`
MASTER_INCREMENT=`echo $MASTER_VERSION | cut -d . -f 3 | cut -d - -f 1`

MERGE_MAJOR=`echo $MERGE_VERSION | cut -d . -f 1`
MERGE_MINOR=`echo $MERGE_VERSION | cut -d . -f 2`
MERGE_INCREMENT=`echo $MERGE_VERSION | cut -d . -f 3 | cut -d - -f 1`

# If merge_major > master_major do nothing
if (( MERGE_MAJOR > MASTER_MAJOR )); then
    echo $MERGE_VERSION
# If merge_minor > master_minor do nothing
elif (( MERGE_MAJOR == MASTER_MAJOR && MERGE_MINOR > MASTER_MINOR )); then
    echo $MERGE_VERSION
elif [[ $3 == *"feature"* ]]; then
    # Bump up minor version
    echo "$MASTER_MAJOR.$((++MASTER_MINOR)).0-SNAPSHOT"
else
    echo "$MASTER_MAJOR.$MASTER_MINOR.$((++MASTER_INCREMENT))-SNAPSHOT"
fi

exit 0