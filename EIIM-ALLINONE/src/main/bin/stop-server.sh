#!/bin/bash

ps -ef | grep com.fs.eiim.EiimApplication | grep -v grep | awk '{print $2}' | xargs kill -9