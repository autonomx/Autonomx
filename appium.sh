#!/bin/bash
cd "${0%/*}"
lsof -t -i :4444 | xargs kill
sleep 3
appium -p 4444