#!/bin/sh

if [ -f deployment/signingkey.asc ]; then
  mvn_args="deploy -Pdeploy -Dgpg.keyname=F0FE0BBE14902B34 --settings deployment/settings.xml"
else
  mvn_args="verify"
fi

./mvnw clean ${mvn_args} --batch-mode --errors --fail-at-end --show-version